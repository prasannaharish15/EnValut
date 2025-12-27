import "./Document.css";
import { useState } from "react";
import { useEffect } from "react";
import axios from "axios";
import api from "../../../api/axios";


function Document() {
  //     const recentDoc = [
  //   { docsName: "Document file", docImg: "/pdf_img.png" },
  //   { docsName: "Word Document file", docImg: "/word_img.png" },
  //   { docsName: "Image file", docImg: "/img_img.png" },
  //   { docsName: "Text File", docImg: "/txt_img.png" }
  // ];

  const imgArr = [
    "/pdf_img.png",
    "/word_img.png",
    "/img_img.png",
    "/txt_img.png",
  ];

  const [allDoc, setAllDoc] = useState([]);

  function getImageIcon(fileType) {
    if (fileType === "pdf") {
      return imgArr[0];
    } else if (fileType === "doc" || fileType === "docx") {
      return imgArr[1];
    } else if (
      fileType === "png" ||
      fileType === "jpg" ||
      fileType === "jpeg" ||
      fileType === "gif"
    ) {
      return imgArr[2];
    } else if (fileType === "txt") {
      return imgArr[3];
    }
  }

  useEffect(() => {
    api
      .get("/api/file/getallfile", {
        headers: {
          Authorization: "Bearer " + localStorage.getItem("token"),
        },
      })
      .then((res) => {
        setAllDoc(res.data);
      })
      .catch((err) => {
        console.error("Error fetching all documents:", err);
      });
  }, []);

  async function handleDownloadFile(fileId, fileName) {
    try {
      const token = localStorage.getItem("token");
      const response = await api.get(
        `api/file/viewordownload/${fileId}`,
        {
          responseType: "blob",
          headers: { Authorization: "Bearer " + token },
        }
      );

      const filename = fileName;

      const blob = new Blob([response.data]);
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = filename;
      document.body.appendChild(a);
      a.click();
      a.remove();
      window.URL.revokeObjectURL(url);
    } catch (err) {
      console.error("Download failed:", err);
      alert("Unable to download file. See console for details.");
    }
  }

  async function handleViewFile(fileId) {
    try {
      const token = localStorage.getItem("token");

      const response = await api.get(
        `api/file/viewordownload/${fileId}`,
        {
          responseType: "blob",
          headers: { Authorization: "Bearer " + token },
        }
      );
      const fileURL = URL.createObjectURL(response.data);
      window.open(fileURL, "_blank");
      setTimeout(() => URL.revokeObjectURL(fileURL), 5000);
    } catch (err) {
      console.error("View failed:", err);
      alert("Unable to open file. See console for details.");
    }
  }
  async function handleDeleteFile(fileId){
    try{
        const token=localStorage.getItem("token");
        const response=await api.delete(`api/file/deletefile/${fileId}`,{
            headers:{
                Authorization:"Bearer "+token
            }
        });
        if(response.data.message=="File deleted successfully"){
            alert("File deleted successfully!");
            setAllDoc(allDoc.filter((doc)=>doc.id!==fileId));
        }
        
        
            }
        catch(err){
            console.error("Delete failed:",err);
            
        }

    }

  

  return (
    <>
      <div>
        <h1>Documents</h1>
        <h2>Recent Documents</h2>
        <div className="recent-documents-container">
          {allDoc.slice(0, 5).map((doc, index) => (
            <div className="recent-documents-main">
              <div className="recent-documents">
                <img
                  className="recent-documents-img"
                  src={getImageIcon(doc.file_type)}
                  alt=""
                />
                <br />
              </div>
              <p>{doc.file_name}</p>
            </div>
          ))}
        </div>
        <h2>All Documents</h2>
        <table className="all-documents">
          <thead>
            <tr>
              <th>Name</th>
              <th>Category</th>
              <th>Upload Date</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {allDoc.map((doc, index) => (
              <tr key={index}>
                <td>{doc.file_name}</td>
                <td>{doc.category_id}</td>
                <td>{doc.created_at}</td>
                <td>
                  {(doc.file_type == "doc" || doc.file_type == "docx") && (
                    <img className="action-class" src="/view.png" alt="" />
                  )}
                  {doc.file_type != "doc" && doc.file_type != "docx" && (
                    <img
                      className="action-class"
                      onClick={() => handleViewFile(doc.id)}
                      src="/view.png"
                      alt=""
                    />
                  )}
                  <img
                    className="action-class"
                    onClick={() => handleDownloadFile(doc.id, doc.file_name)}
                    src="/download.png"
                    alt=""
                  />
                  <img
                    className="action-class"
                    onClick={() => handleDeleteFile(doc.id)}
                    src="/delete.png"
                    alt=""
                  />
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </>
  );
}
export default Document;
