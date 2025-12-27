import { useState } from 'react';
import './register.css';
import axios from "axios";
import { Navigate, useNavigate } from "react-router-dom";
import api from '../../../api/axios';
function Register(){
    
    const [userName,setUserName]=useState();
    const [email,setEmail]=useState();
    const [password,setPassword]=useState();

    async function handleLogin(e){
        e.preventDefault();

        console.log("Register attempted with",userName, email, password);
       try{
        const response=await api.post("/api/auth/register",{
            userName,
            email,
            password,
        });
        console.log("Register successful:", response.message);
       
        alert("Register successful!");
        Navigate("/login");
       }catch(error){
         if (error.response) {
            console.error("Error:", error.response.message);
            alert(error.response.data.message || "email already in use!");
        } else {
            console.error("Network error:", error);
        }
        setUserName("");
        setEmail("");
        setPassword("");
    }
       

    }
 return(
        <>
        <div className="main-body">
            <div className="register-container">
                <h1>Create Account</h1>
                <form action="">
                    <input type="text" placeholder='Username' required onChange={(e)=>{setUserName(e.target.value)}} /> <br />
                    <input type="email" placeholder='Email' required onChange={(e)=>{setEmail(e.target.value)}} /> <br />
                    <input type="password" placeholder='Password' required onChange={(e)=>{setPassword(e.target.value)}} /><br />
                    <button onClick={handleLogin} >Login</button>

                </form>
            </div>

        </div>
        </>
    )

}
export default Register;
