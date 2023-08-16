import { useState } from "react";
import { useEffect } from "react";
//import ReactDOM from 'react-dom/client';
import 'bootstrap/dist/css/bootstrap.css';
import './KafkaEntityDetails.css';
// import NavBar from "./Navbar";
import applicationService from '../sevices/application.service';
import { useParams } from "react-router-dom";
import NavBar from "./Navbar";
import CustomerUploadForm from "./KafkaEntityUploadForm";


function KafkaInfo() {
  const [consumergroup, setConsumerGroup] = useState("");
  const [description, setDescription] = useState("");
  const [emailid, setEmail] = useState("");
  const [monitoringstatus, setMonitoringStatus] = useState("");
  const [owner, setOwner] = useState("");
  const [threshold, setThreshold] = useState("");
  const [topicname, setTopicName] = useState("");
//  const [timestamp, setTimeStamp] = useState("");
  const [cluster, setcluster] = useState([]);
  let [clusterid, selected] = useState("");
  const { groupid } = useParams();

  useEffect(() => {
    fetch('http://localhost:8081/api/getclusterdetails')
      .then(response => response.json())
      .then(data => setcluster(data))
      .catch(error => console.error('Error fetching options:', error));
  },[]);

  const onOptionChangeHandler = (event) => {
    clusterid = event.target.value;
    selected(event.target.value);
    console.log("User Selected Value - ", clusterid);


  }

  const handleChange = (event) => {
    setMonitoringStatus(event.target.value);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const topicInfo = { groupid, consumergroup, description, emailid, monitoringstatus, owner, threshold, topicname, clusterid };
    if (groupid) {
      //update
      applicationService.updateKafka(topicInfo)
        .then(response => {
          console.log(response.data);
        })
        .catch(error => {
          console.log('Something went wrong', error);
        })
    }
    else {
      //create
      applicationService.post(topicInfo)
        .then(response => {
          console.log(response.data);
        })
        .catch(error => {
          console.log('something went wroing', error);
        })
    }
    window.location.href = "/topic"
  };

  useEffect(() => {
    if (groupid) {
      applicationService.getTopicById(groupid)
        .then(topicInfo => {
          setConsumerGroup(topicInfo.data.consumergroup);
          setDescription(topicInfo.data.description);
          setEmail(topicInfo.data.emailid);
          setMonitoringStatus(topicInfo.data.monitoringstatus);
          setOwner(topicInfo.data.owner);
          setThreshold(topicInfo.data.threshold);
          //setTimeStamp(topicInfo.data.timestamp);
          setTopicName(topicInfo.data.topicname);
          selected(topicInfo.data.clusterid);

        })
        .catch(error => {
          console.log('Something went wrong', error);
        })
    }
  },[groupid]);
  return (
    <>
      <NavBar />
      <div className="container mt-3">
        <button type="button" className="btn btn-primary bulk-upload" data-bs-toggle="collapse" data-bs-target="#demo1">Bulk Upload Kafka Topic Details</button>
        <div id="demo1" className="collapse">
          <CustomerUploadForm />
        </div>
      </div>

      <div className="container mt-3">
        <button type="button" className="btn btn-primary bulk-upload upload" data-bs-toggle="collapse" data-bs-target="#demo2">Upload Kafka Topic Details</button>
        <div id="demo2" className="collapse show">
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
                  <select value={monitoringstatus} onChange={handleChange} className="select-monitoring-status">
                    <option value="">Monitoring Status</option>
                    <option value="1">Enable</option>
                    <option value="0">Disable</option>
                  </select>
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
                    type="number"
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

                {/* <div className="col-md-3 field_margins">
                  <label htmlFor="timestamp" className="form-label">Time Stamp</label>
                  <input
                    type="number"
                    className="form-control"
                    value={timestamp}
                    onChange={(e) => setTimeStamp(e.target.value)}
                    placeholder="Enter Time stamp"
                  />
                </div> */}

                <div className="col-md-3 field_margins">
                  <label htmlFor="clusterid" className="form-label">Select Cluster Name</label>
                  <select value={clusterid} onChange={onOptionChangeHandler} className="form-control">
                    <option>Select Cluster for topic</option>
                    {
                      cluster.map(option => (
                        <option key={option.id} value={option.clusterid}>

                          {option.clustername}
                        </option>
                      ))
                    }
                  </select>
                </div><br />

                <button className="btn btn-primary kafkaEntitySubmit_button" >Submit</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </>
  )
}


export default KafkaInfo