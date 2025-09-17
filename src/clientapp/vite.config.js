import { fileURLToPath, URL } from "node:url";

import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import { resolve } from "path";
const root = resolve(__dirname, "src");
const outDir = resolve(
  "C:\\Users\\bbule.SWINGINT\\Documents\\GitHub\\BRK-Car-Sharing\\CarShare\\src\\main\\resources\\templates"
);
// https://vitejs.dev/config/
export default defineConfig({
  root,
  plugins: [vue()],
  build: {
    outDir,
    emptyOutDir: true,
    rollupOptions: {
      input: {
        main: resolve(root, "index.html"),
        administration: resolve(root, "administration/index.html"),
      },
    },
  },
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url)),
    },
  },
  server: {
    proxy: {
      "^/api/(.*)": {
        target: "http://localhost:8989",
        changeOrigin: true,
        secure: false,
      },
    },
  },
});
