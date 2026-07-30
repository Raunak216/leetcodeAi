// @ts-ignore
import "./style.css";

const DASHBOARD_URL = "https://dsafrontend-flax.vercel.app/dashboard";
const LOGIN_URL = "https://dsafrontend-flax.vercel.app";

async function render() {
    const data = (await chrome.storage.local.get("authToken")) as {
        authToken?: string;
    };

    const connected = !!data.authToken;
    const app = document.querySelector<HTMLDivElement>("#app")!;

    app.innerHTML = connected
        ? `
        <div class="popup-wrapper" id="popupWrapper">
            <div class="header">
                <img src="unsheetLogo.svg" alt="unSheet Logo" class="logo-img" />
                <div class="status-pill connected">
                    <span class="pulse-dot"></span>
                    Active
                </div>
            </div>

            <p class="subtitle">
                Your LeetCode progress is being tracked automatically. Head over to the dashboard to explore your insights and progress.
            </p>

            <div class="action-group">
                <button id="dashboard" class="btn btn-primary">
                    Go to Dashboard
                </button>
                <button id="logout" class="btn-link">
                    Stop Tracking
                </button>
            </div>
        </div>
        `
        : `
        <div class="popup-wrapper" id="popupWrapper">
            <div class="header">
                <img src="unsheetLogo.svg" alt="unSheet Logo" class="logo-img" />
                <div class="status-pill disconnected">
                    <span class="pulse-dot"></span>
                    Offline
                </div>
            </div>

           <p class="subtitle">
            Your LeetCode tracking is paused. Visit the website to sign in and resume tracking.
           </p>

            <div class="action-group">
            <button id="login" class="btn btn-primary">
                Reconnect
            </button>
            </div>

        </div>
        `;

    // Event Listeners
    if (connected) {
        document.getElementById("dashboard")?.addEventListener("click", () => {
            chrome.tabs.create({url: DASHBOARD_URL});
        });

        document.getElementById("logout")?.addEventListener("click", async () => {
            const logoutBtn = document.getElementById("logout") as HTMLButtonElement;
            const wrapper = document.getElementById("popupWrapper");

            if (logoutBtn) {
                logoutBtn.disabled = true;
                logoutBtn.innerText = "Disconnecting...";
            }

            await chrome.runtime.sendMessage({type: "LOGOUT"});

            if (wrapper) {
                wrapper.classList.add("fade-out");
                setTimeout(() => {
                    render();
                }, 180);
            } else {
                render();
            }
        });
    } else {
        document.getElementById("login")?.addEventListener("click", () => {
            chrome.tabs.create({url: LOGIN_URL});
        });
    }
}

// Auto re-render if authToken status changes anywhere
chrome.storage.onChanged.addListener((changes, namespace) => {
    if (namespace === "local" && changes.authToken) {
        render();
    }
});

render();