import Login from "./Component/pages/auth/login";
import Register from "./Component/pages/auth/register";
import Dashboard from "./Component/pages/dashboard/dashboard";
import Sidebar from "./Component/sideBar/sidebar";
import ProtectedRoute from "./Component/routes/ProtectedRoute";
import { BrowserRouter, Routes, Route } from "react-router-dom";
function App() {
  return (
    <>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Login />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          {/* <Route path="/sidebar" element={<Sidebar/>}/> */}
          <Route
            path="/dashboard"
            element={
              <ProtectedRoute>
                {" "}
                <div className="app-layout">
                  <Sidebar />
                  <div className="page-content">
                    <Dashboard />
                  </div>
                </div>
              </ProtectedRoute>
            }
          />

          <Route path="*" element={<Login />} />
        </Routes>
      </BrowserRouter>
    </>
  );
}
export default App;
