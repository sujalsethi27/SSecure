async function loadProfile() {

    try {

        const response = await fetch("/api/v7/profile", {
            method: "GET",
            credentials: "include"
        });

        if (!response.ok) {
            window.location.href = "/api/v7/view/login";
            return;
        }

        const user = await response.json();

        document.getElementById("username").innerText =
            `Hey ${user.name} 👋`;

        document.getElementById("email").innerText =
            user.email;

        document.getElementById("avatar").innerText =
            user.name.charAt(0).toUpperCase();

        // Hide Verify button if already verified
        // OR logged in using Google/GitHub
      if (user.accountVerified) {
    document.getElementById("verifyBtn").style.display = "none";
}

    } catch (e) {

        console.error(e);
        window.location.href = "/api/v7/view/login";

    }

}

loadProfile();


// ================= Verify Account =================

document.getElementById("verifyBtn").addEventListener("click", async () => {

    try {

        const response = await fetch("/api/v7/sendVerifyotp", {
            method: "POST",
            credentials: "include"
        });

        if (!response.ok) {
            alert("Unable to send OTP");
            return;
        }

        window.location.href = "/api/v7/view/verifyEmail";

    } catch (e) {

        console.error(e);
        alert("Unable to send OTP.");

    }

});


// ================= Logout =================

document.getElementById("logoutBtn").addEventListener("click", async () => {

    try {

        const response = await fetch("/api/v7/logout", {

            method: "POST",
            credentials: "include"

        });

        if (response.ok) {

            window.location.href = "/api/v7/view/login";

        } else {

            alert("Unable to logout.");

        }

    } catch (e) {

        console.error(e);
        alert("Unable to logout.");

    }

});