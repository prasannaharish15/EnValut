import "./Document.css";

function Document() {
    const recentDoc = [
  { docsName: "Document file", docImg: "/pdf_img.png" },
  { docsName: "Word Document file", docImg: "/word_img.png" },
  { docsName: "Image file", docImg: "/img_img.png" }
];
   const allDoc=[
    {docName:"Document file",category:"Personal",UploadDate:"12-05-2024"},
    {docName:"Word Document file",category:"Work",UploadDate:"10-05-2024"},
    {docName:"Image file",category:"Personal",UploadDate:"08-05-2024"},
    {docName:"Presentation file",category:"Work",UploadDate:"05-05-2024"},
    {docName:"Spreadsheet file",category:"Finance",UploadDate:"01-05-2024"},
    {docName:"PDF file",category:"Education",UploadDate:"28-04-2024"},
    {docName:"Text file",category:"Misc",UploadDate:"25-04-2024"},
    {docName:"Archive file",category:"Backup",UploadDate:"20-04-2024"
    }
   ];

    return(<>
    <div>
        <h1>Documents</h1>
        <h2>Recent Documents</h2>
        <div className="recent-documents-container">
         {recentDoc.map((doc,index)=>(
            <div className="recent-documents-main">
                <div className="recent-documents">
                <img className="recent-documents-img" src={doc.docImg} alt="" /><br />
                </div>
               <p>{doc.docsName}</p>
            </div>    
        ))}
        </div>
        <h2>
            All Documents
        </h2>
        <table className="all-documents">
            <thead>
                <tr>
                    <th>
                        Name
                    </th>
                    <th>
                        Category
                    </th>
                    <th>
                        Upload Date
                    </th>
                    <th>
                        Actions
                    </th>
                </tr>
            </thead>
            <tbody>
                {allDoc.map((doc,index)=>(
                    <tr key={index}>
                    <td>{doc.docName}</td>
                    <td>{doc.category}</td>
                    <td>{doc.UploadDate}</td>
                    <td>
                    </td>
                    </tr>
                    
                ))}

            </tbody>
            
        </table>

       
        


    </div>
    
    </>)

}
export default Document;