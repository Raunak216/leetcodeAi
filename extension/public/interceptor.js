console.log("INTERCEPTOR LOADED");

const originalFetch = window.fetch;

window.fetch = async (...args) => {

    const response = await originalFetch(...args);

    try {

        const url = args[0];

        if (typeof url === "string") {

            if (
                url.includes("interpret_solution") ||
                url.includes("/check/")
            ) {

                const cloned = response.clone();

                const data = await cloned.json();

                window.postMessage({
                    source: "AI_PLACEMENT_ENGINE",
                    url,
                    data
                });

            }
        }

    } catch (e) {
        console.error(e);
    }

    return response;
};