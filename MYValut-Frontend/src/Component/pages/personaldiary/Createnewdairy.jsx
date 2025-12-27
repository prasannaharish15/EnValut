import React, { useState } from "react";
import "./Createnewdairy.css";
import axios from "axios";
function Createnewdairy() {
  const monthNames = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December",
  ];
  const date = new Date();
  const day = date.getDate();
  const month = monthNames[date.getMonth()];
  const year = date.getFullYear();
  const fullDate = `${month} ${day}, ${year}`;
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");

   async function handleSubmit(){
    console.log("Title:", title);
    console.log("Content:", content);
    try{
      const token=localStorage.getItem("token");
      const response=await  axios.post("http://Localhost:8080/api/dairy/save",{
        title:title,
        text:content
      },{
        headers:{
          "Authorization": `Bearer ${token}`
        }
      })
      if(response.data.message==="content saved in db"){
        alert("Diary saved successfully!");
        setTitle("");
        setContent("");
      }

    }
    catch(err){
      console.error("Error saving diary:",err);
      alert("Failed to save diary. Please try again.");

    }
  }

  return (
    <>
      <div className="diary-container">
        <h1 className="diary-heading">Write Your Diary</h1>
        <p className="diary-date">{fullDate}</p>

        <label className="diary-label">Title</label>
        <input onChange={(e)=>setTitle(e.target.value)} type="text" placeholder="Diary Title" className="diary-input" />

        <textarea
          placeholder="Write your thoughts here..."
          className="diary-textarea"
          onChange={(e) => setContent(e.target.value)}
        ></textarea>
        <div>
            <button onClick={()=>handleSubmit()} className="save-btn">Save Diary</button>
        </div>
      </div>
      
    </>
  );
}

export default Createnewdairy;
