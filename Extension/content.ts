console.log("Extension Loaded");

const parts =
    window.location.pathname.split("/");

const problemSlug = parts[2];

console.log(problemSlug);