import { useState } from 'react';
import './login.css';
function Login(){

    const [email,setEmail]=useState();
    const [password,setPassword]=useState();

    async function handleLogin(e){
        e.preventDefault();

        console.log("Login attempted with", email, password);
       // Add login logic here
       try{
        const response=await axios.post("http://localhost:8080/api/login",{
            email,
            password,
        });
        console.log("Login successful:", response.data);
        localStorage.setItem("token",response.data.token);
        alert("Login successful!");
       }catch(error){
         if (error.response) {
            console.error("Error:", error.response.data);
            alert(error.response.data.message || "Invalid email or password!");
        } else {
            console.error("Network error:", error);
            alert("Server not reachable. Please try again later.");
        }
    }
       

    }
 return(
        <>
        <div className="main-body">
            <div className="login-container">
                <img src="/padlock(2).png" alt="" />
                <h1>EnValut</h1>
                <p>Your Secure Knowledge Valut</p>
                <form action="">
                    <input type="email" placeholder='Email' required onChange={(e)=>{setEmail(e.target.value)}} /> <br />
                    <input type="password" placeholder='Password' required onChange={(e)=>{setPassword(e.target.value)}} /><br />
                    <button onClick={handleLogin} >Login</button>

                </form>
                <div>
                    <p>Forget Password? <a href="/register"></a>
                    </p><p>Create account? <a href="/register"></a></p>
                </div>
                    
            </div>

        </div>
        </>
    )

}
export default Login;