import { useState } from "react";
import {useEffect} from "react";
import ReactDOM from 'react-dom/client';
import axios from "axios";
import 'bootstrap/dist/css/bootstrap.css';
import './KafkaEntityDetails.css';
import NavBar from "./Navbar";
//import SelectClusterName from "./SelectClusterName";


function KafkaInfo() {
  const [consumergroup, setConsumerGroup] = useState("");
  const [description, setDescription] = useState("");
  const [emailid, setEmail] = useState("");
  const [monitoringstatus, setMonitoringStatus] = useState("");
  const [owner, setOwner] = useState("");
  const [threshold, setThreshold] =useState("");
  const [topicname, setTopicName] =useState("");
  const [timestamp, setTimeStamp] =useState("");
  const [values,setvalues] =useState([])

  useEffect(()=>{
    fetch("http://localhost:8081/api/getclustername").then((data)=>data.json()).then((val)=>setvalues(val))
  },[] )
  const onOptionChangeHandler = (event) => {
		console.log("User Selected Value - ", event.target.value)
	}
  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post('http://localhost:8081/api/post/kafka', { consumergroup,description,emailid, monitoringstatus,owner, threshold,topicname,timestamp,values});
      console.log(response.data);


    } catch (error) {
      console.error(error);
    }

  };
  return (
    <>
    <NavBar/>
    <div className="container col-md-12">
      <form onSubmit={handleSubmit} className="design-form">
        <div className="row">
            {/* <h2>Kafka Details</h2> */}
            <div className="col-md-3">
              <div className="form-group">
                <label htmlFor="consumergroup" className="form-label">ConsumerGroup</label>
                  <input
                    type="text"
                    className="form-control" 
                    value={consumergroup}
                    onChange={(e) => setConsumerGroup(e.target.value)}
                    placeholder="Enter ConsumerGroup Name"
                />
              </div>
            </div>

            <div className="col-md-3">
              <div className="form-group">
                <label htmlFor="description" className="form-label">Description</label> 
                  <input
                    type="text" 
                    className="form-control"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    placeholder="Description"
                />
              </div>
            </div>   

            <div className="col-md-3">
              <label htmlFor="emailid" className="form-label">Email ID</label> 
              <input
                type="email" 
                className="form-control"
                value={emailid}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="Enter Email ID"
              />   
            </div>

            <div className="col-md-3">
              <label htmlFor="monitoringstatus" className="form-label">Monitoring Status</label> 
              <input
                type="text" 
                className="form-control"
                value={monitoringstatus}
                onChange={(e) => setMonitoringStatus(e.target.value)}
                placeholder="Monitoring Status 0 or 1"
              />  
              <div className="clearfix"></div> 
            </div>

            <div className="col-md-3 field_margins">
              <label htmlFor="owner" className="form-label">Owner</label> 
              <input
                type="text" 
                className="form-control"
                value={owner}
                onChange={(e) => setOwner(e.target.value)}
                placeholder="Owner Name"
              />
            </div>

            <div className="col-md-3 field_margins">
              <label htmlFor="threshold" className="form-label">Threshold Value</label> 
              <input
                type="text" 
                className="form-control"
                value={threshold}
                onChange={(e) => setThreshold(e.target.value)}
                placeholder="Enter threshole value"
              />
            </div>

            <div className="col-md-3 field_margins">
              <label htmlFor="topicname" className="form-label">Topic Name</label> 
              <input
                type="text"
                className="form-control" 
                value={topicname}
                onChange={(e) => setTopicName(e.target.value)}
                placeholder="Enter Topic Name"
              />
            </div>

            <div className="col-md-3 field_margins">
              <label htmlFor="timestamp" className="form-label">Time Stamp</label> 
              <input
                type="text"
                className="form-control" 
                value={timestamp}
                onChange={(e) => setTimeStamp(e.target.value)}
                placeholder="Enter Time stamp"
              />
            </div>

            <div className="col-md-3 field_margins">
              <label htmlFor="topicname" className="form-label">Select Cluster Name</label> 
              <select onChange={onOptionChangeHandler} className="form-control">
              <option>Select Cluster for topic</option>
                {
                  values.map((options,i)=><option>
                    {options}
                  </option>)
                }
              </select>
            </div><br/>
            <button className="btn btn-primary kafkaEntitySubmit_button" onClick={event =>  window.location.href='http://localhost:3000/topic'}>Submit</button>     
        </div>
      </form>
    </div>
    </>
  )
}

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(<KafkaInfo />);

export default KafkaInfo