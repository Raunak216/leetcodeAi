import { defineConfig } from "vite";
import { resolve } from "node:path";

export default defineConfig({
    build: {
        rollupOptions: {
            input: {
                popup: resolve("index.html"),
                content: resolve("src/content.ts"),
                background: resolve("src/background.ts"),
            },
            output: {
                entryFileNames: (chunkInfo) => {
                    if(chunkInfo.name==="content")
                        return "content.js";
                    if(chunkInfo.name==="background")
                        return "background.js";
                    return "assets/[name]-[hash].js";

                },
            },
        },
    },
});