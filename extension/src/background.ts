import {BACKEND_URL} from "./config";

console.log("AlgoLens Background Started");
let authToken: string | null = null;

// Initialize token on startup
async function initToken() {
    const data = await chrome.storage.local.get("authToken");
    if (data.authToken) {
        authToken = data.authToken as string;
        console.log("Token restored from storage");
    }
}

initToken();

export async function getToken(): Promise<string | undefined> {
    const data = await chrome.storage.local.get("authToken");
    return data.authToken as string | undefined;
}

export async function setToken(token: string | null) {
    authToken = token;
    await chrome.storage.local.set({authToken});
}

async function savePendingAttempt(payload: any) {
    const data = await chrome.storage.local.get("pendingAttempts");
    const pending = (data.pendingAttempts || []) as any[];

    const exists = pending.some(
        (p: any) => p.questionSlug === payload.questionSlug && p.journeyJson === payload.journeyJson
    );

    if (!exists) {
        pending.push(payload);
    }

    await chrome.storage.local.set({pendingAttempts: pending});
}

async function uploadAttempt(payload: any) {
    try {
        if (!authToken) {
            authToken = (await getToken()) || null;
        }

        if (!authToken) {
            console.log("No auth token available, saving pending attempt");
            await savePendingAttempt(payload);
            return;
        }

        const res = await fetch(`${BACKEND_URL}/attempts`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${authToken}`
            },
            body: JSON.stringify(payload)
        });

        if (res.status === 401) {
            await setToken(null);
            await savePendingAttempt(payload);
            return;
        }

        if (!res.ok) {
            console.log("STATUS:", res.status);
            console.log("BODY:", await res.text());
            throw new Error("Upload Failed");
        }

        console.log("Attempt Uploaded successfully");
    } catch (e) {
        console.error("Upload Error", e);
        await savePendingAttempt(payload);
    }
}

async function retryPendingAttempts() {
    const data = await chrome.storage.local.get("pendingAttempts");
    const pending = (data.pendingAttempts || []) as any[];
    if (!pending.length) return;

    const remaining: any[] = [];

    if (!authToken) {
        authToken = (await getToken()) || null;
    }

    for (const payload of pending) {
        try {
            if (!authToken) {
                remaining.push(payload);
                continue;
            }

            const res = await fetch(`${BACKEND_URL}/attempts`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${authToken}`
                },
                body: JSON.stringify(payload)
            });

            if (!res.ok) {
                remaining.push(payload);
            }
        } catch {
            remaining.push(payload);
        }
    }
    await chrome.storage.local.set({pendingAttempts: remaining});
}

// 1. Internal messages (from content.js / popup)
chrome.runtime.onMessage.addListener((message) => {
    console.log("📥 [Background] Internal message received:", message.type);
    if (message.type === "UPLOAD_ATTEMPT") {
        console.log("🚀 [Background] Triggering upload attempt with payload:", message.payload);
        uploadAttempt(message.payload);
    }
    return true;
});

// 2. External messages (FROM NEXT.JS FRONTEND)
chrome.runtime.onMessageExternal.addListener((message) => {
    console.log("🌍 [Background] External message intercepted:", message);
    if (message.type === "SET_AUTH_TOKEN") {
        console.log("🔑 [Background] Found token in external message. Saving...");
        setToken(message.token).then(() => {
            console.log("✅ [Background] Token successfully written to storage. Retrying pending syncs.");
            return retryPendingAttempts();
        });
    }
    return true;
});