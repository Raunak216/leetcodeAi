const script = document.createElement("script");
let latestCode = "";
let latestLanguage = "";
// @ts-ignore
script.src = chrome.runtime.getURL(
    "interceptor.js"
);

(document.head || document.documentElement)
    .appendChild(script);

console.log("AI Placement Engine Loaded");

let currentPath = location.pathname;

function extractProblemData() {

    const match = location.pathname.match(
        /^\/problems\/([^/]+)/
    );
    if (!match) return null;
    const slug = match[1];

    const titleElement = document.querySelector(
        'a[href^="/problems/"]'
    );

    const rawTitle =
        titleElement?.textContent?.trim() ?? "";

    const title = rawTitle.replace(
        /^\d+\.\s*/,
        ""
    );

    const difficultyElement = document.querySelector(
        '[class*="difficulty"]'
    );

    const difficulty =
        difficultyElement?.textContent?.trim();

    return {
        slug,
        title,
        difficulty
    };
}

function handleQuestionChange() {

    const problem = extractProblemData();

    if (!problem) return;

    console.log("Problem Opened");

    console.log(problem);
}

handleQuestionChange();

setInterval(() => {

    if (location.pathname !== currentPath) {

        currentPath = location.pathname;

        handleQuestionChange();
    }

}, 1000);


window.addEventListener(
    "message",
    (event) => {

        if (event.source !== window)
            return;

        if (
            event.data?.source !==
            "AI_PLACEMENT_ENGINE"
        )
            return;

        const url =
            event.data.url;

        const requestBody =
            event.data.requestBody;

        const responseData =
            event.data.responseData;

        // RUN START

        if (
            url.includes(
                "interpret_solution"
            )
        ) {
            latestCode =
                requestBody?.typed_code ?? "";

            latestLanguage =
                requestBody?.lang ?? "";
            console.log(
                "RUN STARTED"
            );


            console.log(
                requestBody
            );

            return;
        }

        // RUN/SUBMIT RESULT

        if (
            responseData?.state ===
            "SUCCESS"
        ) {

            if (
                responseData?.state ===
                "SUCCESS"
            ) {

                let eventType = "UNKNOWN";

                if (
                    responseData.task_name ===
                    "judger.runcodetask.RunCode"
                ) {
                    eventType = "RUN";
                }
                else if (
                    responseData.task_name ===
                    "judger.judgetask.Judge"
                ) {
                    eventType = "SUBMIT";
                }

                const attempt = {

                    questionSlug:
                    extractProblemData()?.slug,

                    title:
                    extractProblemData()?.title,

                    topic:
                        "UNKNOWN",

                    difficulty:
                    extractProblemData()?.difficulty,

                    language:
                    latestLanguage,

                    verdict:
                    responseData.status_msg,

                    eventType,

                    runtime:
                        Number(
                            responseData.display_runtime
                        ) || null,

                    memory:
                        responseData.memory ?? null,

                    code:
                        latestCode ?? null,

                    attempts: 1,

                    timeSpent: 0,

                    accepted:
                        responseData.status_msg ===
                        "Accepted",

                    userId: 1
                };

                console.log(
                    "SENDING TO BACKEND",
                    attempt
                );

                fetch(
                    "http://localhost:8080/attempts",
                    {
                        method: "POST",

                        headers: {
                            "Content-Type":
                                "application/json"
                        },

                        body:
                            JSON.stringify(
                                attempt
                            )
                    }
                )
                    .then(res => res.json())
                    .then(data =>
                        console.log(
                            "Saved:",
                            data
                        )
                    )
                    .catch(err =>
                        console.error(
                            err
                        )
                    );
            }
        }
    }
);