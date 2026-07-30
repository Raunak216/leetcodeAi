import {BACKEND_URL} from "./config";

let authToken: string | null = null;

// Initialize token on startup
async function initToken() {
    const data = await chrome.storage.local.get("authToken");
    if (data.authToken) {
        authToken = data.authToken as string;
    }
}

initToken();

export async function getToken(): Promise<string | undefined> {
    const data = await chrome.storage.local.get("authToken");
    return data.authToken as string | undefined;
}

export async function setToken(token: string | null) {
    authToken = token;
    if (token) {
        await chrome.storage.local.set({
            authToken: token
        });
    } else {
        await chrome.storage.local.remove("authToken");
    }
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
            throw new Error("Upload Failed");
        }

    } catch (e) {
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
    switch (message.type) {
        case "UPLOAD_ATTEMPT":
            uploadAttempt(message.payload);
            break;

        case "LOGOUT":
            setToken(null);
            break;
    }
    return true;
});


// 2. External messages (FROM NEXT.JS FRONTEND)
chrome.runtime.onMessageExternal.addListener((message, _sender, sendResponse) => {
    if (message.type === "SET_AUTH_TOKEN") {
        setToken(message.token).then(() => {
            retryPendingAttempts();
            sendResponse({success: true});
        });
        return true;
    }
    if (message.type === "LOGOUT") {
        setToken(null).then(() => {
            sendResponse({success: true});
        });
        return true;
    }
});