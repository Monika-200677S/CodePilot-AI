// ============================================================
// api.js — shared fetch helper + auth/session utilities
// ============================================================

const API_BASE = window.location.origin + "/api";

function getToken() {
    return localStorage.getItem("cm_token");
}

function getSession() {
    const raw = localStorage.getItem("cm_user");
    return raw ? JSON.parse(raw) : null;
}

function setSession(authResponse) {
    localStorage.setItem("cm_token", authResponse.token);
    localStorage.setItem("cm_user", JSON.stringify({
        name: authResponse.name,
        email: authResponse.email,
        role: authResponse.role
    }));
}

function clearSession() {
    localStorage.removeItem("cm_token");
    localStorage.removeItem("cm_user");
}

function requireAuth() {
    if (!getToken()) {
        window.location.href = "login.html";
    }
}

function logout() {
    clearSession();
    window.location.href = "login.html";
}

// Generic authenticated fetch wrapper. Throws with a readable message on failure.
async function apiFetch(path, options = {}) {
    const headers = Object.assign(
        { "Content-Type": "application/json" },
        options.headers || {}
    );

    const token = getToken();
    if (token) {
        headers["Authorization"] = "Bearer " + token;
    }

    const response = await fetch(API_BASE + path, Object.assign({}, options, { headers }));

    if (response.status === 401 || response.status === 403) {
        clearSession();
        window.location.href = "login.html";
        throw new Error("Session expired. Please log in again.");
    }

    let data = null;
    const text = await response.text();
    if (text) {
        try { data = JSON.parse(text); } catch (e) { data = text; }
    }

    if (!response.ok) {
        const message = (data && data.message) ? data.message : "Something went wrong. Please try again.";
        throw new Error(message);
    }

    return data;
}

// Renders the sidebar user chip + highlights the active nav link.
function renderUserChip(elementId, activePage) {
    const session = getSession();
    const el = document.getElementById(elementId);
    if (el && session) {
        const initial = session.name ? session.name.charAt(0).toUpperCase() : "?";
        el.innerHTML = `
            <div class="avatar">${initial}</div>
            <div>
                <div style="font-weight:500;">${session.name}</div>
                <div style="color:var(--text-muted); font-size:11px;">${session.email}</div>
            </div>
        `;
    }
    document.querySelectorAll(".nav-link").forEach(link => {
        if (link.dataset.page === activePage) {
            link.classList.add("active");
        }
    });
    const adminLink = document.getElementById("adminNavLink");
    if (adminLink && session && session.role === "ADMIN") {
        adminLink.style.display = "flex";
    }
}
