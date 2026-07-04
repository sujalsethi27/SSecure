const otpInputs = document.querySelectorAll(".otp-box");

otpInputs.forEach((input, index) => {

    input.addEventListener("input", () => {

 // Keep only digits
        input.value = input.value.replace(/[^0-9]/g, "");

        if(input.value.length === 1 && index < otpInputs.length-1){

            otpInputs[index+1].focus();

        }

    });

    input.addEventListener("keydown",(e)=>{

        if(e.key==="Backspace" && input.value==="" && index>0){

            otpInputs[index-1].focus();

        }

    });

});