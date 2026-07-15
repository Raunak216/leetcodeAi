import { BACKEND_URL } from "./config";

console.log("AlgoLens Background Started");

export async function getToken() {

    const data =
        await chrome.storage.local.get(
            "authToken"
        );

    return (
        data.authToken as string | undefined
    );
}

export async function setToken(
    token: string | null
) {

    await chrome.storage.local.set({

        authToken: token

    });
}
retryPendingAttempts();

async function savePendingAttempt(payload: any) {
    const data = await chrome.storage.local.get("pendingAttempts");

    const pending = (data.pendingAttempts || []) as any[];
    pending.push(payload);

    await chrome.storage.local.set({
        pendingAttempts: pending,
    });
}

async function uploadAttempt(payload: any) {
    try {
        const token =
            await getToken();

        const res =
            await fetch(
                `${BACKEND_URL}/attempts`,
                {
                    method:"POST",
                    headers:{
                        "Content-Type":"application/json",
                        Authorization:
                            `Bearer ${token}`
                    },
                    body:JSON.stringify(payload)
                }
            );

        if (!res.ok) {

            console.log(
                "Status",
                res.status
            );

            console.log(
                "Response",
                await res.text()
            );

            throw new Error("Upload Failed");
        }

        console.log(
            "Attempt Uploaded",
            await res.json(),
        );
    } catch (e) {
        console.error("Upload Error", e);
        await savePendingAttempt(payload);    }
}

chrome.runtime.onMessage.addListener(async (message) => {
    switch (message.type) {
        case "UPLOAD_ATTEMPT":
            await uploadAttempt(message.payload);
            await retryPendingAttempts();
            break;
    }
});

async function retryPendingAttempts() {
    const data = await chrome.storage.local.get("pendingAttempts");

    const pending = (data.pendingAttempts || []) as any[];
    if (!pending.length) return;

    const remaining: any[] = [];

    for (const payload of pending) {
        try {
            const token =
                await getToken();

            const res =
                await fetch(
                    `${BACKEND_URL}/attempts`,
                    {
                        method:"POST",
                        headers:{
                            "Content-Type":"application/json",
                            Authorization:
                                `Bearer ${token}`
                        },
                        body:JSON.stringify(payload)
                    }
                );

            if (!res.ok) {
                remaining.push(payload);
            }
        } catch {
            remaining.push(payload);
        }
    }

    await chrome.storage.local.set({
        pendingAttempts: remaining,
    });
}

 chrome.runtime.onMessage.addListener((message) => {

    switch (message.type) {

        case "SET_AUTH_TOKEN":

             setToken(
                message.token
            );

            break;
    }

});
