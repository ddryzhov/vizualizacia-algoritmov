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
