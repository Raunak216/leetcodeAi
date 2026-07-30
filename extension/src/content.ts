import {diffLines} from "diff";

// console.log(
//     "STORAGE",
//     chrome.storage
// );

const script = document.createElement("script");

script.src = chrome.runtime.getURL(
    "interceptor.js"
);

(document.head || document.documentElement)
    .appendChild(script);

// console.log("unSheet loaded");

interface JourneyStep {
    eventType: string;
    verdict: string;
    codeDiff: string;
    runtime?: number;
    memory?: number;
    passedTestCases?: number | null;
    totalTestCases?: number | null;
    timestamp: string;
}

interface CurrentJourney {
    questionSlug: string;
    title: string;
    difficulty: string;
    language: string;
    startedAt: string;

    lastCode: string;

    pendingDiff: string;

    steps: JourneyStep[];
}

let journeys: Record<
    string,
    CurrentJourney
> = {};

let currentPath = location.pathname;
let latestLanguage = "";

async function saveJourneys() {

    await chrome.storage.local.set({
        journeys
    });
}

async function loadJourneys() {

    const data =
        await chrome.storage.local.get(
            "journeys"
        );

    if (
        data.journeys
    ) {

        journeys =
            (data.journeys ??
                {}) as Record<
                string,
                CurrentJourney
            >;

        for (const slug in journeys) {

            journeys[slug].lastCode ??= "";

            journeys[slug].pendingDiff ??= "";

            journeys[slug].steps ??= [];
        }

        // console.log(
        //     "Journeys Restored",
        //     journeys
        // );
    }
}

function extractProblemData() {

    const match =
        location.pathname.match(
            /^\/problems\/([^/]+)/
        );

    if (!match) return null;

    const slug = match[1];

    const titleElement =
        document.querySelector(
            'a[href^="/problems/"]'
        );

    const rawTitle =
        titleElement?.textContent?.trim() ?? "";

    const title =
        rawTitle.replace(
            /^\d+\.\s*/,
            ""
        );

    const difficultyElement =
        document.querySelector(
            '[class*="difficulty"]'
        );

    const difficulty =
        difficultyElement?.textContent?.trim() ??
        "UNKNOWN";

    return {
        slug,
        title,
        difficulty
    };
}

function startJourney() {

    const problem =
        extractProblemData();

    if (!problem)
        return;

    if (
        journeys[
            problem.slug
            ]
    ) {
        return;
    }


    journeys[
        problem.slug
        ] = {

        questionSlug:
        problem.slug,

        title:
        problem.title,

        difficulty:
        problem.difficulty,
        pendingDiff: "",
        language: "",

        startedAt:
            new Date()
                .toISOString(),

        lastCode: "",

        steps: []
    };

    // console.log(
    //     "Journey Started",
    //     journeys[
    //         problem.slug
    //         ]
    // );
}

function getCurrentJourney() {

    const problem =
        extractProblemData();

    if (!problem)
        return null;

    return journeys[
        problem.slug
        ];
}

function buildDiff(
    oldCode?: string,
    newCode?: string
) {

    const safeOld =
        oldCode ?? "";

    const safeNew =
        newCode ?? "";

    const changes =
        diffLines(
            safeOld,
            safeNew
        );

    let result = "";

    for (const part of changes) {

        if (part.added) {

            result +=
                "+ " +
                part.value +
                "\n";
        } else if (part.removed) {

            result +=
                "- " +
                part.value +
                "\n";
        }
    }

    return result;
}

(async () => {

    await loadJourneys();

    startJourney();

})();

setInterval(() => {

    if (
        location.pathname !==
        currentPath
    ) {

        currentPath =
            location.pathname;

        startJourney();
    }

}, 1000);

window.addEventListener(
    "message",
    async (event) => {

        if (
            event.source !==
            window
        ) return;

        if (event.data?.source !== "UNSHEET") return;

        const url =
            event.data.url;

        const requestBody =
            event.data.requestBody;

        const responseData =
            event.data.responseData;
        if (
            url.includes(
                "interpret_solution"
            )
        ) {

            const problem =
                extractProblemData();

            if (!problem) {
                return;
            }

            const currentCode =
                requestBody?.typed_code ?? "";

            const journey =
                getCurrentJourney();

            if (!journey) {
                return;
            }

            const codeDiff =
                buildDiff(
                    journey.lastCode,
                    currentCode
                );


            journey.pendingDiff =
                codeDiff;

            journey.lastCode =
                currentCode;

            latestLanguage =
                requestBody?.lang ?? "";

            journey.language =
                latestLanguage;

            await saveJourneys();


            // console.log(
            //     "RUN STARTED"
            // );

            return;
        }

        if (
            responseData?.state !==
            "SUCCESS"
        ) {
            return;
        }


        const taskName =
            responseData.task_name;

        let eventType = "";

        if (
            taskName?.includes(
                "RunCode"
            )
        ) {

            eventType = "RUN";
        } else if (
            taskName?.includes(
                "Judge"
            )
        ) {

            eventType = "SUBMIT";
        }

        const problem =
            extractProblemData();

        if (!problem) {
            return;
        }

        const journey =
            getCurrentJourney();

        if (!journey) {
            return;
        }

        const codeDiff =
            journey.pendingDiff;
        const runtime =

            Number(
                responseData.display_runtime
            )

            ||

            Number(
                responseData.status_runtime?.replace(
                    " ms",
                    ""
                )
            )

            ||

            0;
        const step: JourneyStep = {

            eventType,

            verdict:
            responseData.status_msg,

            codeDiff,

            runtime,

            memory:
                Number(
                    responseData.memory
                ) || 0,

            passedTestCases:
                responseData.total_correct ?? null,

            totalTestCases:
                responseData.total_testcases ?? null,

            timestamp:
                new Date().toISOString()
        };
        if (
            eventType !== "RUN" &&
            eventType !== "SUBMIT"
        ) {
            return;
        }

        journey.steps.push(
            step
        );
        journey.pendingDiff =
            "";

        await saveJourneys();
        // console.log(
        //     "TOTAL STEPS",
        //     journey.steps.length,
        //     "STEP ADDED",
        //     step
        // );

        const accepted =

            eventType ===
            "SUBMIT"

            &&

            responseData.status_msg ===
            "Accepted";


        if (
            accepted &&
            journey
        ) {

            const payload = {

                questionSlug:
                journey.questionSlug,

                title:
                journey.title,

                difficulty:
                journey.difficulty,

                language:
                journey.language,

                runtime:
                    Number(
                        responseData.display_runtime
                    ) || 0,

                memory:
                    Number(
                        responseData.memory
                    ) || 0,

                journeyJson:
                    JSON.stringify({

                        startedAt:
                        journey.startedAt,

                        completedAt:
                            new Date()
                                .toISOString(),

                        steps:
                        journey.steps
                    })
            };

            // console.log(
            //     "SENDING JOURNEY",
            //     payload
            // );

            chrome.runtime.sendMessage({

                type: "UPLOAD_ATTEMPT",

                payload

            });

            delete journeys[
                journey.questionSlug
                ];

            await saveJourneys();
        }
    }
);