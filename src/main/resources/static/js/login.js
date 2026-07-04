document.getElementById("loginForm").addEventListener("submit", async function(e){

    e.preventDefault();

    const email=document.getElementById("email").value;

    const password=document.getElementById("password").value;

    const error=document.getElementById("errorMessage");

    error.innerText="";

    try{

        const response=await fetch("/api/v7/login",{

            method:"POST",

            headers:{
                "Content-Type":"application/json"
            },

            credentials:"include",

            body:JSON.stringify({
                email:email,
                password:password
            })

        });

        if(response.ok){

            window.location.href="/api/v7/view/dashboard";

        }else{

            const data=await response.json();

            error.innerText=data.error;

        }

    }catch(err){

        error.innerText="Something went wrong.";

    }

});