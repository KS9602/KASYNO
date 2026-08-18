

const form = document.querySelector("#formik");

form.addEventListener("submit", (event) => {
    event.preventDefault();
    sendData()
})

async function sendData(){
    const formData = new FormData(form)
    try{
        const response = await fetch("localhost:8080/xxx",{
        method: "POST",
        headers:{
        'Access-Control-Allow-Origin': '*'
        }
        body: formData,
        });
        console.log(await response.json())
    }
    catch(e){
        console.error(e)
    }
}