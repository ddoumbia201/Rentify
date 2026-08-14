// This function retrieves the CSRF token from the cookies. It looks for a cookie named "XSRF-TOKEN" and returns its value if found. If the cookie is not present, it returns null.
function getCsrfToken() {
    const match = document.cookie.match(/XSRF-TOKEN=([^;]+)/); // Regular expression to find the "XSRF-TOKEN" cookie
    return match ? match[1] : null; // Return the value of the cookie if found, otherwise return null
}