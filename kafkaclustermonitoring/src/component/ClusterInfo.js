import React, { useState,useEffect } from 'react';
//import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.css';
import './ClusterInfo.css';
import NavBar from './Navbar';
import applicationService from '../sevices/application.service';
import {useNavigate, useParams } from "react-router-dom";

const Clusterinfo = () => {
  const [clustername, setClusterName] = useState('');
  const [monitoringstatus, setClusterMonitoringStatus] = useState('');
  const [bootstrap_servers, setBootstrapServer] = useState('');
  const [zookeeper_servers, setZookeeperServer] = useState('');
  const [broker_logs_dir, setBrokerLogsDir] = useState('');
  const [zoo_logs_dir, setZookeeperLogsDir] = useState('');
  const navigate = useNavigate();
  const {clusterid} = useParams();

  const handleSubmit = async (e) => {
    e.preventDefault();
    const clusterInfo = {clusterid, clustername,monitoringstatus,bootstrap_servers,zookeeper_servers,broker_logs_dir,zoo_logs_dir};
    if(clusterid)
    {
        //update
        applicationService.update(clusterInfo)
            .then(response =>{
                console.log(response.data);
                navigate('/');
            })
            .catch(error => {
                console.log('Something went wrong', error);
            }) 
    }
    else
    {  
        //create
        applicationService.create(clusterInfo)
            .then(response => {
                console.log(response.data);
                navigate("/");
            })
            .catch(error => {
                console.log('something went wroing', error);
            })
    }

  }

  useEffect(() => {
    if(clusterid) {
        applicationService.get(clusterid)
            .then(clusterInfo => {
                setClusterName(clusterInfo.data.clustername);
                setClusterMonitoringStatus(clusterInfo.data.monitoringstatus);
                setZookeeperServer(clusterInfo.data.zookeeper_servers);
                setZookeeperLogsDir(clusterInfo.data.zoo_logs_dir);
                setBootstrapServer(clusterInfo.data.bootstrap_servers);
                setBrokerLogsDir(clusterInfo.data.broker_logs_dir);
            })
            .catch(error => {
                console.log('Something went wrong', error);
            })
    }
 }, [])



  return (
    <>
    <NavBar/>
    <div className="container col-md-12">
      <form className='form-design'>
        <div className='row'>
            {/* <h1>Cluster Details</h1> */}
            <div className='col-md-3'>
              <label htmlFor="clustername">Cluster Name</label> 
                <input
                  required
                  type="text" 
                  className='form-control'
                  id="clusterName"
                  value={clustername}
                  onChange={(e) => setClusterName(e.target.value)}
                  placeholder="Enter Cluster Name"                
              />
            </div>        

            <div className='col-md-3'>
              <label htmlFor='monitoringstatus' >Cluster Monitoring Status</label> 
              <input
                type="text"
                className='form-control' 
                id="monitoringstatus"
                value={monitoringstatus}
                onChange={(e) => setClusterMonitoringStatus(e.target.value)}
                placeholder="Enter Monitoring Status 0 or 1"
                required
              />
            </div>      

            <div className='col-md-3'>
              <label htmlFor='zookeeper_servers' >Zookeeper Server Port</label> 
              <input
                type="text"
                className='form-control' 
                id="zookeeper_servers"
                value={zookeeper_servers}
                onChange={(e) => setZookeeperServer(e.target.value)}
                placeholder="Enter Zookeeper server port"
                required
              />
            </div>

            <div className='col-md-3'>
              <label htmlFor='bootstrap_servers' >Bootstrap Server Port</label> 
              <input
                type="text"
                className='form-control' 
                id="bootstrap_servers"
                value={bootstrap_servers}
                onChange={(e) => setBootstrapServer(e.target.value)}
                placeholder="Enter bootstrap server port"
                required
              />
            </div>

            <div className='col-md-6 field_margins'>
              <label htmlFor='zoo_logs_dir' >Zookeeper Server Logs Directory</label> 
              <input
                type="text"
                className='form-control' 
                id="zoo_logs_dir"
                value={zoo_logs_dir}
                onChange={(e) => setZookeeperLogsDir(e.target.value)}
                placeholder="Enter Zookeeper Server Logs Directory"
                required
              />
            </div> 

            <div className='col-md-6 field_margins'>
              <label htmlFor='broker_logs_dir' >Broker Server Log Directory</label> 
              <input
                type="text"
                className='form-control' 
                id="broker_logs_dir"
                value={broker_logs_dir}
                onChange={(e) => setBrokerLogsDir(e.target.value)}
                placeholder="Enter Broker Server Log Directory"
                required
              />
            </div> 

            <button type="submit" className='btn btn-primary cluster_submit' onClick={(e) => handleSubmit(e)}>Submit</button>
        </div>
      </form>      
    </div>
    </>
  );
};

export default Clusterinfo;
