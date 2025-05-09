import axios from "axios";
import { getApiBaseUrl } from "./apiConfig";

const API_BASE = getApiBaseUrl();

const api = axios.create({
    baseURL: `${API_BASE}/api`,
    headers: { "Content-Type": "application/json" }
});

export default api;
