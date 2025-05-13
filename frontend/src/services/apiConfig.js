/**
 * Determine the API base URL based on the current window location.
 *
 * - In development (localhost or 127.0.0.1), directs to port 8080.
 * - On GitHub Pages (hostname ending in “github.io”), uses the Railway production URL.
 * - Otherwise, uses the same host and port as the current page.
 *
 * @returns {string} The base URL for API calls.
 */
export function getApiBaseUrl() {
    const { protocol, hostname, port } = window.location;

    if (hostname === "localhost" || hostname === "127.0.0.1") {
        return `${protocol}//${hostname}:8080`;
    }

    if (hostname.endsWith("github.io")) {
        return "https://vizualizacia-algoritmov-production.up.railway.app";
    }

    return `${protocol}//${hostname}${port ? `:${port}` : ""}`;
}
