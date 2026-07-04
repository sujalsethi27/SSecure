document.getElementById("verifyForm").addEventListener("submit", async function(e){

    e.preventDefault();

    const otp = [...document.querySelectorAll(".otp-box")]
        .map(input => input.value)
        .join("");

    const error = document.getElementById("errorMessage");

    error.innerText = "";

    try{

        const response = await fetch("/api/v7/verifyOtp",{

            method:"POST",

            headers:{
                "Content-Type":"application/json"
            },

            credentials:"include",

            body:JSON.stringify({
                otp:otp
            })

        });

        if(response.ok){

            alert("Account verified successfully!");

            window.location.href="/api/v7/view/dashboard";

        }else{

            error.innerText="Invalid OTP";

        }

    }catch(err){

        error.innerText="Something went wrong.";

    }

});