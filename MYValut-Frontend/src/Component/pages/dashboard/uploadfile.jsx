import "./uploadfile.css";
import { useState } from "react";
function UploadFile() {

    const [file,setFile]=useState(null);

    function handleFileChange(e){
        setFile(e.target.files[0]);
        console.log("Selected file:", e.target.files[0]);
    }

    function handleFileUpload(){
        if(!file){
            alert("Please select a file to upload.");
            return;
        }
        
    }






  return (
    <>
      <div className="uploadfile-main">
        
        <div className="uploadfile-card">

        <div id="fileupoad">
            <label htmlFor="file-input" id="label-input">Choose a file to Upload</label>
            <input type="file" id="file-input" onChange={handleFileChange} accept=".pdf, .doc, .docx, .txt"/>
          </div>

          

          <div className="uploadfile-content">
            <label >Category</label>
            <select name="category" id="category-select">
              <option value="">--Select-</option>
              <option value="Personal">Personal</option>
              <option value="Study">Study</option>
              <option value="Work">Work</option>
              <option value="Others">Others</option>

            </select>
            <br />

            <label>Tags</label>
            <input type="text" id="tags-input" />
            <br />
            <button onClick={handleFileUpload}>Upload File</button>
          </div>
        </div>
      </div>
    </>
  );
}
export default UploadFile;
