// ============================================================
// COURSE MANAGEMENT SYSTEM - FRONTEND JAVASCRIPT
// ============================================================

// Backend URL
const API_BASE_URL = "http://localhost:8080";

// ============================================================
// GLOBAL VARIABLES
// ============================================================

let courses = [];
let filteredCourses = [];

let currentUser = null;
let editingCourseId = null;

// ============================================================
// PAGE LOAD
// ============================================================

document.addEventListener("DOMContentLoaded", function () {

    initializeApplication();

});


// ============================================================
// INITIALIZE APPLICATION
// ============================================================

function initializeApplication() {

    loadUserFromStorage();

    setupNavigation();

    setupForms();

    setupSearchAndFilter();

    setupModalEvents();

    loadCourses();

    updateNavigation();

}


// ============================================================
// LOCAL STORAGE / AUTHENTICATION
// ============================================================

function loadUserFromStorage() {

    const accessToken = localStorage.getItem("accessToken");

    if (!accessToken) {

        currentUser = null;

        return;
    }

    try {

        const payload = decodeJwt(accessToken);

        currentUser = {

            email: payload.sub || payload.email || "User",

            role:
                payload.role ||
                payload.roles ||
                "STUDENT"

        };

        if (Array.isArray(currentUser.role)) {

            currentUser.role = currentUser.role[0];

        }

        currentUser.role = String(currentUser.role)
            .replace("ROLE_", "")
            .toUpperCase();

    } catch (error) {

        console.error("Unable to read access token:", error);

        currentUser = null;

    }

}


// ============================================================
// JWT DECODER
// ============================================================

function decodeJwt(token) {

    const parts = token.split(".");

    if (parts.length !== 3) {

        throw new Error("Invalid JWT token");

    }

    const payload = parts[1];

    const base64 = payload
        .replace(/-/g, "+")
        .replace(/_/g, "/");

    const jsonPayload = decodeURIComponent(
        atob(base64)
            .split("")
            .map(function (character) {

                return (
                    "%" +
                    ("00" + character.charCodeAt(0).toString(16))
                        .slice(-2)
                );

            })
            .join("")
    );

    return JSON.parse(jsonPayload);

}


// ============================================================
// NAVIGATION
// ============================================================

function setupNavigation() {

    const mobileMenuButton =
        document.querySelector(".mobile-menu-btn");

    const navLinks =
        document.querySelector(".nav-links");

    if (mobileMenuButton && navLinks) {

        mobileMenuButton.addEventListener(
            "click",
            function () {

                navLinks.classList.toggle("active");

            }
        );

    }

    document.querySelectorAll(".nav-links a").forEach(
        function (link) {

            link.addEventListener(
                "click",
                function () {

                    if (navLinks) {

                        navLinks.classList.remove("active");

                    }

                }
            );

        }
    );

}


// ============================================================
// UPDATE NAVIGATION
// ============================================================

function updateNavigation() {

    const loginButton =
        document.querySelector(".login-nav");

    const registerButton =
        document.querySelector(".register-nav");

    const logoutButton =
        document.querySelector(".logout-nav");

    const dashboardLink =
        document.querySelector(".dashboard-link");

    if (currentUser) {

        if (loginButton) {

            loginButton.style.display = "none";

        }

        if (registerButton) {

            registerButton.style.display = "none";

        }

        if (logoutButton) {

            logoutButton.style.display = "inline-flex";

        }

        if (dashboardLink) {

            dashboardLink.style.display = "inline-flex";

        }

    } else {

        if (loginButton) {

            loginButton.style.display = "inline-flex";

        }

        if (registerButton) {

            registerButton.style.display = "inline-flex";

        }

        if (logoutButton) {

            logoutButton.style.display = "none";

        }

        if (dashboardLink) {

            dashboardLink.style.display = "none";

        }

    }

}


// ============================================================
// LOGIN
// ============================================================

async function loginUser(event) {

    event.preventDefault();

    const email =
        document.getElementById("loginEmail").value.trim();

    const password =
        document.getElementById("loginPassword").value;

    const errorElement =
        document.getElementById("loginError");

    const loginButton =
        document.getElementById("loginButton");

    if (errorElement) {

        errorElement.textContent = "";

    }

    if (!email || !password) {

        showLoginError("Please enter email and password.");

        return;

    }

    if (loginButton) {

        loginButton.disabled = true;

        loginButton.textContent = "Logging in...";

    }

    try {

        const response = await fetch(
            API_BASE_URL + "/api/auth/login",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({
                    email: email,
                    password: password
                })
            }
        );

        const data = await response.json();

        if (!response.ok) {

            throw new Error(
                data.message ||
                data.error ||
                "Login failed."
            );

        }

        const accessToken =
            data.accessToken ||
            data.token ||
            data.jwt;

        const refreshToken =
            data.refreshToken;

        if (!accessToken) {

            throw new Error(
                "Access token was not returned by the server."
            );

        }

        localStorage.setItem(
            "accessToken",
            accessToken
        );

        if (refreshToken) {

            localStorage.setItem(
                "refreshToken",
                refreshToken
            );

        }

        loadUserFromStorage();

        updateNavigation();

        closeModal("loginModal");

        showToast("Login successful!");

        document.getElementById("loginForm").reset();

        updateDashboard();

    } catch (error) {

        console.error("Login error:", error);

        showLoginError(error.message);

    } finally {

        if (loginButton) {

            loginButton.disabled = false;

            loginButton.textContent = "Login";

        }

    }

}


// ============================================================
// LOGIN ERROR
// ============================================================

function showLoginError(message) {

    const errorElement =
        document.getElementById("loginError");

    if (errorElement) {

        errorElement.textContent = message;

    }

}


// ============================================================
// LOGOUT
// ============================================================

function logoutUser() {

    localStorage.removeItem("accessToken");

    localStorage.removeItem("refreshToken");

    currentUser = null;

    updateNavigation();

    showToast("You have been logged out.");

    updateDashboard();

}


// ============================================================
// REGISTER
// ============================================================

async function registerUser(event) {

    event.preventDefault();

    const name =
        document.getElementById("registerName").value.trim();

    const email =
        document.getElementById("registerEmail").value.trim();

    const password =
        document.getElementById("registerPassword").value;

    const role =
        document.getElementById("registerRole").value;

    const errorElement =
        document.getElementById("registerError");

    if (errorElement) {

        errorElement.textContent = "";

    }

    if (!name || !email || !password) {

        showRegisterError(
            "Please fill in all required fields."
        );

        return;

    }

    try {

        const response = await fetch(
            API_BASE_URL + "/api/auth/register",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({

                    name: name,

                    email: email,

                    password: password,

                    role: role

                })
            }
        );

        const data = await response.json();

        if (!response.ok) {

            throw new Error(
                data.message ||
                data.error ||
                "Registration failed."
            );

        }

        closeModal("registerModal");

        document
            .getElementById("registerForm")
            .reset();

        showToast(
            "Registration successful. Please login."
        );

        openModal("loginModal");

    } catch (error) {

        console.error(
            "Registration error:",
            error
        );

        showRegisterError(error.message);

    }

}


// ============================================================
// REGISTER ERROR
// ============================================================

function showRegisterError(message) {

    const errorElement =
        document.getElementById("registerError");

    if (errorElement) {

        errorElement.textContent = message;

    }

}


// ============================================================
// LOAD COURSES
// ============================================================

async function loadCourses() {

    try {

        const response = await fetch(
            API_BASE_URL + "/api/courses"
        );

        if (!response.ok) {

            throw new Error(
                "Unable to load courses."
            );

        }

        const data = await response.json();

        if (Array.isArray(data)) {

            courses = data;

        } else if (data && Array.isArray(data.content)) {

            courses = data.content;

        } else {

            courses = [];

        }

        filteredCourses = [...courses];

        displayCourses();

        updateDashboard();

    } catch (error) {

        console.error(
            "Course loading error:",
            error
        );

        /*
         * We don't create fake courses here.
         * The frontend should display the actual
         * data returned by the Spring Boot backend.
         */

        courses = [];

        filteredCourses = [];

        displayCourses();

        showToast(
            "Unable to connect to the course server."
        );

    }

}


// ============================================================
// DISPLAY COURSES
// ============================================================

function displayCourses() {

    const container =
        document.getElementById("coursesContainer");

    const emptyCourses =
        document.getElementById("emptyCourses");

    if (!container) {

        return;

    }

    container.innerHTML = "";

    if (
        !filteredCourses ||
        filteredCourses.length === 0
    ) {

        if (emptyCourses) {

            emptyCourses.style.display = "block";

        }

        return;

    }

    if (emptyCourses) {

        emptyCourses.style.display = "none";

    }

    filteredCourses.forEach(
        function (course) {

            const courseId =
                getCourseId(course);

            const courseCard = document.createElement("div");

            courseCard.className = "course-card";

            courseCard.innerHTML = `

                <div class="course-image">

                    ${getCourseIcon(course.category)}

                </div>

                <div class="course-body">

                    <span class="course-category">

                        ${escapeHtml(
                            course.category || "General"
                        )}

                    </span>

                    <h3>
                        ${escapeHtml(
                            course.name || "Untitled Course"
                        )}
                    </h3>

                    <p>
                        ${escapeHtml(
                            course.description ||
                            "No description available."
                        )}
                    </p>

                    <div class="course-meta">

                        <span>
                            ⏱
                            ${escapeHtml(
                                course.duration ||
                                "Flexible"
                            )}
                        </span>

                        <span>
                            👨‍🏫
                            ${escapeHtml(
                                course.trainer ||
                                "Trainer"
                            )}
                        </span>

                    </div>

                    <div class="course-actions">

                        <button
                            class="btn btn-primary btn-full"
                            onclick="viewCourse(${courseId})">

                            View Course

                        </button>

                    </div>

                </div>

            `;

            container.appendChild(courseCard);

        }
    );

}


// ============================================================
// COURSE ID HELPER
// ============================================================

function getCourseId(course) {

    if (!course) {

        return null;

    }

    return (
        course.id ??
        course.courseId ??
        course.courseID
    );

}


// ============================================================
// COURSE ICON
// ============================================================

function getCourseIcon(category) {

    const value =
        String(category || "")
            .toLowerCase();

    if (value.includes("java")) {

        return "☕";

    }

    if (
        value.includes("web") ||
        value.includes("html") ||
        value.includes("css")
    ) {

        return "🌐";

    }

    if (
        value.includes("database") ||
        value.includes("sql") ||
        value.includes("mysql")
    ) {

        return "🗄️";

    }

    if (
        value.includes("spring") ||
        value.includes("backend")
    ) {

        return "⚙️";

    }

    if (
        value.includes("javascript") ||
        value.includes("js")
    ) {

        return "📜";

    }

    return "📚";

}


// ============================================================
// ESCAPE HTML
// ============================================================

function escapeHtml(value) {

    if (
        value === null ||
        value === undefined
    ) {

        return "";

    }

    return String(value)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");

}


// ============================================================
// SEARCH AND FILTER
// ============================================================

function setupSearchAndFilter() {

    const searchInput =
        document.getElementById("courseSearch");

    const categorySelect =
        document.getElementById("courseCategory");

    if (searchInput) {

        searchInput.addEventListener(
            "input",
            filterCourses
        );

    }

    if (categorySelect) {

        categorySelect.addEventListener(
            "change",
            filterCourses
        );

    }

}


// ============================================================
// FILTER COURSES
// ============================================================

function filterCourses() {

    const searchInput =
        document.getElementById("courseSearch");

    const categorySelect =
        document.getElementById("courseCategory");

    const searchTerm =
        searchInput
            ? searchInput.value
                .trim()
                .toLowerCase()
            : "";

    const selectedCategory =
        categorySelect
            ? categorySelect.value
            : "";

    filteredCourses =
        courses.filter(
            function (course) {

                const name =
                    String(
                        course.name || ""
                    ).toLowerCase();

                const description =
                    String(
                        course.description || ""
                    ).toLowerCase();

                const category =
                    String(
                        course.category || ""
                    ).toLowerCase();

                const matchesSearch =
                    !searchTerm ||
                    name.includes(searchTerm) ||
                    description.includes(searchTerm) ||
                    category.includes(searchTerm);

                const matchesCategory =
                    !selectedCategory ||
                    category ===
                    selectedCategory.toLowerCase();

                return (
                    matchesSearch &&
                    matchesCategory
                );

            }
        );

    displayCourses();

}


// ============================================================
// VIEW COURSE
// ============================================================

function viewCourse(courseId) {

    const course =
        courses.find(
            function (item) {

                return String(
                    getCourseId(item)
                ) === String(courseId);

            }
        );

    if (!course) {

        showToast("Course not found.");

        return;

    }

    const message =

        "Course: " +
        (course.name || "N/A") +
        "\n\n" +

        "Category: " +
        (course.category || "N/A") +
        "\n\n" +

        "Description: " +
        (course.description || "N/A") +
        "\n\n" +

        "Duration: " +
        (course.duration || "Flexible");

    alert(message);

}


// ============================================================
// DASHBOARD
// ============================================================

function updateDashboard() {

    const dashboardTitle =
        document.getElementById("dashboardTitle");

    const dashboardSubtitle =
        document.getElementById("dashboardSubtitle");

    const userAvatar =
        document.getElementById("userAvatar");

    const totalCourses =
        document.getElementById("totalCourses");

    const myCourses =
        document.getElementById("myCourses");

    const studentPanel =
        document.getElementById("studentPanel");

    const trainerPanel =
        document.getElementById("trainerPanel");

    const adminPanel =
        document.getElementById("adminPanel");

    if (!currentUser) {

        if (dashboardTitle) {

            dashboardTitle.textContent =
                "Please Login";

        }

        if (dashboardSubtitle) {

            dashboardSubtitle.textContent =
                "Login to access your dashboard.";

        }

        if (userAvatar) {

            userAvatar.textContent = "G";

        }

        if (totalCourses) {

            totalCourses.textContent =
                courses.length;

        }

        if (myCourses) {

            myCourses.textContent = "0";

        }

        if (studentPanel) {

            studentPanel.style.display = "none";

        }

        if (trainerPanel) {

            trainerPanel.style.display = "none";

        }

        if (adminPanel) {

            adminPanel.style.display = "none";

        }

        return;

    }

    const role =
        String(
            currentUser.role || "STUDENT"
        ).toUpperCase();

    if (dashboardTitle) {

        dashboardTitle.textContent =
            "Welcome, " +
            currentUser.email;

    }

    if (dashboardSubtitle) {

        dashboardSubtitle.textContent =
            "Role: " + role;

    }

    if (userAvatar) {

        userAvatar.textContent =
            currentUser.email
                .charAt(0)
                .toUpperCase();

    }

    if (totalCourses) {

        totalCourses.textContent =
            courses.length;

    }

    if (myCourses) {

        myCourses.textContent = "0";

    }

    if (studentPanel) {

        studentPanel.style.display =
            role === "STUDENT"
                ? "block"
                : "none";

    }

    if (trainerPanel) {

        trainerPanel.style.display =
            role === "TRAINER"
                ? "block"
                : "none";

    }

    if (adminPanel) {

        adminPanel.style.display =
            role === "ADMIN"
                ? "block"
                : "none";

    }

    displayStudentCourses();

    displayTrainerCourses();

}


// ============================================================
// STUDENT COURSE TABLE
// ============================================================

function displayStudentCourses() {

    const table =
        document.getElementById(
            "studentCoursesTable"
        );

    if (!table) {

        return;

    }

    table.innerHTML = "";

    courses.forEach(
        function (course) {

            const row =
                document.createElement("tr");

            row.innerHTML = `

                <td>
                    ${escapeHtml(
                        course.name || "N/A"
                    )}
                </td>

                <td>
                    ${escapeHtml(
                        course.category || "N/A"
                    )}
                </td>

                <td>
                    0
                </td>

            `;

            table.appendChild(row);

        }
    );

}


// ============================================================
// TRAINER COURSE TABLE
// ============================================================

function displayTrainerCourses() {

    const table =
        document.getElementById(
            "trainerCoursesTable"
        );

    if (!table) {

        return;

    }

    table.innerHTML = "";

    courses.forEach(
        function (course) {

            const courseId =
                getCourseId(course);

            const row =
                document.createElement("tr");

            row.innerHTML = `

                <td>
                    ${escapeHtml(
                        course.name || "N/A"
                    )}
                </td>

                <td>
                    ${escapeHtml(
                        course.category || "N/A"
                    )}
                </td>

                <td>
                    0
                </td>

                <td>

                    <button
                        class="btn btn-outline"
                        onclick="editCourse(${courseId})">

                        Edit

                    </button>

                    <button
                        class="btn btn-danger"
                        onclick="deleteCourse(${courseId})">

                        Delete

                    </button>

                </td>

            `;

            table.appendChild(row);

        }
    );

}


// ============================================================
// ADD / EDIT COURSE
// ============================================================

function openCourseModal(courseId = null) {

    const modal =
        document.getElementById("courseModal");

    const title =
        document.getElementById(
            "courseModalTitle"
        );

    const form =
        document.getElementById("courseForm");

    const hiddenId =
        document.getElementById("courseId");

    if (!modal) {

        return;

    }

    editingCourseId = courseId;

    if (form) {

        form.reset();

    }

    if (courseId !== null) {

        const course =
            courses.find(
                function (item) {

                    return String(
                        getCourseId(item)
                    ) === String(courseId);

                }
            );

        if (!course) {

            showToast("Course not found.");

            return;

        }

        if (title) {

            title.textContent =
                "Edit Course";

        }

        if (hiddenId) {

            hiddenId.value =
                getCourseId(course);

        }

        setInputValue(
            "courseName",
            course.name
        );

        setInputValue(
            "courseDescription",
            course.description
        );

        setInputValue(
            "courseCategoryInput",
            course.category
        );

        setInputValue(
            "courseDuration",
            course.duration
        );

    } else {

        if (title) {

            title.textContent =
                "Add Course";

        }

        if (hiddenId) {

            hiddenId.value = "";

        }

    }

    modal.classList.add("active");

}


// ============================================================
// EDIT COURSE
// ============================================================

function editCourse(courseId) {

    openCourseModal(courseId);

}


// ============================================================
// SAVE COURSE
// ============================================================

async function saveCourse(event) {

    event.preventDefault();

    const name =
        getInputValue("courseName");

    const description =
        getInputValue("courseDescription");

    const category =
        getInputValue("courseCategoryInput");

    const duration =
        getInputValue("courseDuration");

    if (!name) {

        showToast("Course name is required.");

        return;

    }

    const courseData = {

        name: name,

        description: description,

        category: category,

        duration: duration

    };

    try {

        let url =
            API_BASE_URL + "/api/courses";

        let method = "POST";

        if (editingCourseId !== null) {

            url =
                API_BASE_URL +
                "/api/courses/" +
                editingCourseId;

            method = "PUT";

        }

        const response = await fetch(
            url,
            {
                method: method,

                headers: getAuthHeaders(),

                body: JSON.stringify(courseData)
            }
        );

        const data =
            await parseResponse(response);

        if (!response.ok) {

            throw new Error(
                data.message ||
                data.error ||
                "Unable to save course."
            );

        }

        closeModal("courseModal");

        showToast(
            editingCourseId !== null
                ? "Course updated successfully."
                : "Course created successfully."
        );

        editingCourseId = null;

        await loadCourses();

    } catch (error) {

        console.error(
            "Save course error:",
            error
        );

        showToast(error.message);

    }

}


// ============================================================
// DELETE COURSE
// ============================================================

async function deleteCourse(courseId) {

    const confirmed =
        confirm(
            "Are you sure you want to delete this course?"
        );

    if (!confirmed) {

        return;

    }

    try {

        const response = await fetch(
            API_BASE_URL +
            "/api/courses/" +
            courseId,
            {
                method: "DELETE",

                headers: getAuthHeaders()
            }
        );

        const data =
            await parseResponse(response);

        if (!response.ok) {

            throw new Error(
                data.message ||
                data.error ||
                "Unable to delete course."
            );

        }

        showToast(
            "Course deleted successfully."
        );

        await loadCourses();

    } catch (error) {

        console.error(
            "Delete course error:",
            error
        );

        showToast(error.message);

    }

}


// ============================================================
// AUTH HEADERS
// ============================================================

function getAuthHeaders() {

    const headers = {

        "Content-Type": "application/json"

    };

    const token =
        localStorage.getItem("accessToken");

    if (token) {

        headers.Authorization =
            "Bearer " + token;

    }

    return headers;

}


// ============================================================
// PARSE RESPONSE
// ============================================================

async function parseResponse(response) {

    const text =
        await response.text();

    if (!text) {

        return {};

    }

    try {

        return JSON.parse(text);

    } catch (error) {

        return {

            message: text

        };

    }

}


// ============================================================
// FORM SETUP
// ============================================================

function setupForms() {

    const loginForm =
        document.getElementById("loginForm");

    const registerForm =
        document.getElementById("registerForm");

    const courseForm =
        document.getElementById("courseForm");

    const contactForm =
        document.getElementById("contactForm");

    if (loginForm) {

        loginForm.addEventListener(
            "submit",
            loginUser
        );

    }

    if (registerForm) {

        registerForm.addEventListener(
            "submit",
            registerUser
        );

    }

    if (courseForm) {

        courseForm.addEventListener(
            "submit",
            saveCourse
        );

    }

    if (contactForm) {

        contactForm.addEventListener(
            "submit",
            handleContactForm
        );

    }

}


// ============================================================
// CONTACT FORM
// ============================================================

function handleContactForm(event) {

    event.preventDefault();

    const name =
        getInputValue("contactName");

    const email =
        getInputValue("contactEmail");

    const message =
        getInputValue("contactMessage");

    if (!name || !email || !message) {

        showToast(
            "Please fill in all contact fields."
        );

        return;

    }

    showToast(
        "Thank you! Your message has been received."
    );

    event.target.reset();

}


// ============================================================
// MODAL SETUP
// ============================================================

function setupModalEvents() {

    document.querySelectorAll(
        ".modal-close"
    ).forEach(
        function (button) {

            button.addEventListener(
                "click",
                function () {

                    const modal =
                        button.closest(".modal");

                    if (modal) {

                        modal.classList.remove(
                            "active"
                        );

                    }

                }
            );

        }
    );

    document.querySelectorAll(
        ".modal"
    ).forEach(
        function (modal) {

            modal.addEventListener(
                "click",
                function (event) {

                    if (
                        event.target === modal
                    ) {

                        modal.classList.remove(
                            "active"
                        );

                    }

                }
            );

        }
    );

}


// ============================================================
// OPEN MODAL
// ============================================================

function openModal(modalId) {

    const modal =
        document.getElementById(modalId);

    if (modal) {

        modal.classList.add("active");

    }

}


// ============================================================
// CLOSE MODAL
// ============================================================

function closeModal(modalId) {

    const modal =
        document.getElementById(modalId);

    if (modal) {

        modal.classList.remove("active");

    }

}


// ============================================================
// INPUT HELPERS
// ============================================================

function getInputValue(id) {

    const element =
        document.getElementById(id);

    return element
        ? element.value.trim()
        : "";

}


function setInputValue(id, value) {

    const element =
        document.getElementById(id);

    if (element) {

        element.value =
            value === null ||
            value === undefined
                ? ""
                : value;

    }

}


// ============================================================
// TOAST
// ============================================================

function showToast(message) {

    const toast =
        document.getElementById("toast");

    const toastMessage =
        document.getElementById("toastMessage");

    if (!toast) {

        alert(message);

        return;

    }

    if (toastMessage) {

        toastMessage.textContent =
            message;

    }

    toast.classList.add("show");

    setTimeout(
        function () {

            toast.classList.remove("show");

        },
        3000
    );

}


// ============================================================
// GLOBAL FUNCTIONS
// ============================================================

window.loginUser = loginUser;

window.registerUser = registerUser;

window.logoutUser = logoutUser;

window.openModal = openModal;

window.closeModal = closeModal;

window.openCourseModal = openCourseModal;

window.editCourse = editCourse;

window.deleteCourse = deleteCourse;

window.viewCourse = viewCourse;

window.filterCourses = filterCourses;

window.showToast = showToast;