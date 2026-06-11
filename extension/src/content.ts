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