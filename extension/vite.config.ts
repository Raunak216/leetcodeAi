import { defineConfig } from "vite";
import { resolve } from "node:path";

export default defineConfig({
    build: {
        rollupOptions: {
            input: {
                popup: resolve("index.html"),
                content: resolve("src/content.ts"),
            },
            output: {
                entryFileNames: (chunkInfo) =>
                    chunkInfo.name === "content"
                        ? "content.js"
                        : "assets/[name]-[hash].js",
            },
        },
    },
});