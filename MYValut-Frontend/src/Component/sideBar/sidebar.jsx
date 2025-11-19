import "./sidebar.css";
function Sidebar() {
  return (
    <>
      <div className="sidebar-main">
        <div className="sidebar-content">

          <div className="sidebar-logo">

            <h1>EnVault</h1>
          </div>
          
          <div className="sidebar-icon">
            <img src="/dashboard_img.png" alt="" />
            <p>Dashboard</p>
          </div>

          <div className="sidebar-icon">
            <img src="/dashboard_img.png" alt="" />
            <p>Document</p>
          </div>
          <div className="sidebar-icon">
            <img src="/dashboard_img.png" alt="" />
            <p>Notes</p>
          </div>

          <div className="sidebar-icon">
            <img src="/dashboard_img.png" alt="" />
            <p>Categories</p>
          </div>

          <div className="sidebar-icon">
            <img src="/dashboard_img.png" alt="" />
            <p>Analytics</p>
          </div>

          <div className="sidebar-icon">
            <img src="/dashboard_img.png" alt="" />
            <p>Settings</p>
          </div>
        </div>
      </div>
    </>
  );
}
export default Sidebar;
