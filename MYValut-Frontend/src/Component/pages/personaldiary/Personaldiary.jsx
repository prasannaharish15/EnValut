import "./personaldiary.css";
import { useNavigate } from "react-router-dom";
function Personaldiary() {
    const navigate=useNavigate();

    function handleNavigate(path){
      navigate(path);
    }
  return (
    <>
      <div>
        <div className="head-section">
          <div>
            <h2>Personal Diary</h2>
          </div>
          <div>
            <button onClick={()=>{handleNavigate("/createnewdairy")}}> Create New Dairy</button>
          </div>
        </div>
      </div>
    </>
  );
}
export default Personaldiary;
