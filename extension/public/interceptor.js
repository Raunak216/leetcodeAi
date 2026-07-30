const originalFetch = window.fetch;

window.fetch = async (...args) => {

    const url = args[0];

    let requestBody = null;

    if (args[1]?.body) {

        try {

            requestBody = JSON.parse(
                args[1].body
            );

        } catch (e) {
        }

    }

    const response =
        await originalFetch(...args);

    try {

        if (
            typeof url === "string" &&
            (
                url.includes("interpret_solution") ||
                url.includes("/check/") ||
                url.includes("/submit/")
            )
        ) {

            const cloned =
                response.clone();

            const responseData =
                await cloned.json();

            window.postMessage({

                source: "UNSHEET",

                url,

                requestBody,

                responseData

            });

        }

    } catch (e) {


    }

    return response;
};