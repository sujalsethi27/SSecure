document.getElementById("registerForm").addEventListener("submit", async function(e){

    e.preventDefault();

    const name = document.getElementById("name").value.trim();
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirmPassword").value;

    const error = document.getElementById("errorMessage");
    error.innerText = "";

    if(password !== confirmPassword){
        error.innerText = "Passwords do not match";
        return;
    }

    try{

        const response = await fetch("/api/v7/create",{

            method:"POST",

            headers:{
                "Content-Type":"application/json"
            },

            body:JSON.stringify({
                name:name,
                email:email,
                password:password
            })

        });

        if(response.ok){

   alert("Account created successfully! Welcome to Authify. Please login to continue.");
   
        window.location.href="/api/v7/view/login";
        }else{

            const data = await response.json();

            error.innerText = data.message || data.error || "Registration failed";

        }

    }catch(err){

        error.innerText="Something went wrong.";

    }

});