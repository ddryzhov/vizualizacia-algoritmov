import axios from "axios";
import { getApiBaseUrl } from "./apiConfig";

/**
 * Axios HTTP client configured with dynamic base URL and JSON headers.
 * Base URL is determined by window.location via getApiBaseUrl(),
 * then appends the "/api" prefix for backend endpoints.
 */
const API_BASE = getApiBaseUrl();

const api = axios.create({
    baseURL: `${API_BASE}/api`,
    headers: { "Content-Type": "application/json" }
});

export default api;
