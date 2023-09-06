import 'bootstrap/dist/css/bootstrap.css';
import './ClusterInfo.css';
import NavBar from './Navbar';

import { useState } from "react";
import {useNavigate, useParams } from "react-router-dom";
import { useEffect } from "react";
import applicationService from '../sevices/application.service';

const AddCluster = () => {
  const [clustername, setClusterName] = useState('');
  const navigate = useNavigate();
  const {id} = useParams();

  const handleSubmit = async (e) => {
    e.preventDefault();
    const clusterInfo = {id, clustername};

    if(id)
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
    if(id) {
        applicationService.get(id)
            .then(clusterInfo => {
                setClusterName(clusterInfo.data.clustername);
            })
            .catch(error => {
                console.log('Something went wrong', error);
            })
    }
 })

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
  
            {/* <button type="submit" className='btn btn-primary cluster_submit' onClick={event =>  window.location.href='http://192.168.1.30:3000/kafkalist'}>Submit</button> */}
            <button onClick={(e) => handleSubmit(e)} className="btn btn-primary kafkaEntitySubmit_button">Save</button>
        </div>
      </form>      
    </div>
    </>
  );
};

export default AddCluster;
