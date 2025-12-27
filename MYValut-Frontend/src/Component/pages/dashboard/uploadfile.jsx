import "./uploadfile.css";
import { useState, useRef } from "react";
import api from "../../../api/axios";

function UploadFile() {

  const [file, setFile] = useState(null);
  const fileInputRef = useRef(null);

  function handleFileChange(e) {
    setFile(e.target.files[0]);
    console.log("Selected file:", e.target.files[0]);
  }

  async function handleFileUpload() {
    if (!file) {
      alert("Please select a file to upload.");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);

    const token = localStorage.getItem("token");
    if (!token) {
      alert("Please login first.");
      return;
    }

    try {
      const response = await api.post(
        "/api/file/uploadfile",
        formData,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "multipart/form-data",
          },
        }
      );

      const data = response.data;

      if (data.message === "file uploaded successfully") {
        alert("File uploaded successfully!");
      } else {
        alert("File upload failed. Please try again.");
      }

    
      setFile(null);
      if (fileInputRef.current) {
        fileInputRef.current.value = "";
      }

    } catch (error) {
      console.error("Error uploading file:", error);
      alert("Error uploading file");
    }
  }

  return (
    <>
      <div className="uploadfile-main">
        <div className="uploadfile-card">

          <div id="fileupoad">
            <label htmlFor="file-input" id="label-input">
              Choose a file to Upload
            </label>
            <input
              type="file"
              id="file-input"
              ref={fileInputRef}
              onChange={handleFileChange}
              accept=".pdf,.doc,.docx,.txt,.jpeg,.png,.jpg"
            />
          </div>

          <div className="uploadfile-content">
            <label>Category</label>
            <select name="category" id="category-select">
              <option value="">--Select--</option>
              <option value="Personal">Personal</option>
              <option value="Study">Study</option>
              <option value="Work">Work</option>
              <option value="Others">Others</option>
            </select>

            <br />

            <label>Tags</label>
            <input type="text" id="tags-input" />

            <br />

            <button onClick={handleFileUpload}>
              Upload File
            </button>
          </div>

        </div>
      </div>
    </>
  );
}

export default UploadFile;
