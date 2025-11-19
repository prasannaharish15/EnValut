import { use } from "react";
import "./dashboard.css";
import { useNavigate } from "react-router-dom";

function dashboard() {
    const navigate=useNavigate();

    function handleUploadClick() {
        navigate("/uploadFile");
    }

  return (
    <>
      <div className="dashboard-content">
        <h1>Welcome back,<br />Prasanna</h1>
        
        <p>Your secure knowledge & documens at a glance</p>
        <div className="dashboard-line">
          <div className="dashboard-size">
            <button onClick={handleUploadClick} >+ Upload</button>
          </div>
          <div className="dashboard-size">
            <button>New Notes</button>
          </div>
          <div className="dashboard-size">
            <button>Create Category</button>
          </div>
        </div>
        <div className="dashboard-box">

          <div className="dashboard-box-item1"></div>
          <br />
          <div className="dashboard-box-item1">
            <div className="dashboard-box-item1-1"></div>
            <div className="dashboard-box-item1-2"></div>
          </div>
        </div>
      </div>
    </>
  );
}
export default dashboard;
