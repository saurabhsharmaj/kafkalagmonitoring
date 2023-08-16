import React, { useEffect, useState } from 'react';
import './TopicRecord.css';
// import NavBar from "./Navbar";
import { Link} from 'react-router-dom';
import { FaEdit, FaPlus, FaTrash } from "react-icons/fa";
import applicationService from '../sevices/application.service';
//import axios from 'axios';
import NavBar from './Navbar';

const TopicList = () => {

    const [TopicRecord, setTopicRecord] = useState([]);
  
//    const params = useParams();
    
    const init = clusterid => {
      applicationService.getTopic()
        .then(response => {
          console.log('Printing Topic data', response.data);
          setTopicRecord(response.data);
        })
        .catch(error => {
          console.log('Something went wrong', error);
        }) 
    }
  
    useEffect(() => {
      init();
    }, []);

    // useEffect(() => {
    //     const fetchData = async () => {
    //       try {
    //             await axios.get(applicationService.gettopiccluster(`${params.id}`))
    //         .then(res => {
    //                      console.log(res)
    //                      setTopicRecord(res.data)
    //         })
    //         // Process the response data
    //       } catch (error) {

    //         console.log(error.config); // Config used to make the request
    //       }
    //     };
    
    //     fetchData();
    //   }, [params.id])

    // useEffect(()=> {
    //     axios.get(applicationService.gettopiccluster(`${params.id}`))
    //     .then(res => {
    //         console.log(res)
    //         setTopicRecord(res.data)
    //     })
    //     .catch(err =>{
    //         console.log(err)
    //     })
    // }, [params.id])

    const handleDelete = (groupid) => {
        console.log('Printing id', groupid);
        if(window.confirm("Do you want to delete...??")){
            applicationService.removetopic(groupid)
            .then(response => {
            console.log(response.data);
            init();            
          })
          .catch(error => {
            console.log('Something went wrong', error);
          })
        }
    }
    
    return(
        <>
        <NavBar/>
            <div className="container">
                <div className="row">
               
                  <table className="table border-table">
                      <thead>
                          <tr>
                              <th>ID</th>                                
                              <th>Consumer Group</th>  
                              <th>Topic Name</th>
                              <th>Owner</th>                              
                              <th>Email Id</th>                                
                              <th>Monitoring Status</th>                                
                              <th>Threshold Value</th>                                
                              <th>Timestamp</th>                                
                              <th>Description</th>                                        
                              <th>Action</th>
                          </tr>
                      </thead>

                      <tbody>
                          {
                              TopicRecord.map(topic => (
                                  <tr key={topic.groupid}>
                                      <td>
                                          {topic.groupid}
                                          <Link to={`/kafka/edit/${topic.groupid}`}>
                                              <FaEdit className='icon_click'/>
                                          </Link>      
                                      </td>
                                      <td>{topic.consumergroup}</td>
                                      <td>{topic.topicname}</td>
                                      <td>{topic.owner}</td>
                                      <td>{topic.emailid}</td>
                                      <td>{(topic.monitoringstatus === 1) ? "Enable" : "Disable"}</td>
                                      <td>{topic.threshold}</td>
                                      <td>{topic.timestamp}</td>
                                      <td>{topic.description}</td>
                                      <td>
                                          <Link to>
                                          <FaTrash className='icon_click' onClick={() => {
                                              handleDelete(topic.groupid);
                                              }}/>
                                          </Link>
                                      </td>
                                  </tr>
                              ))
                          }                                   
                      </tbody>
                  </table>
                  <Link to = '/Kafka' className='add-topic'><FaPlus/>Add Kafka Topic</Link>
                </div>
            </div>

        </>
    )
}

export default TopicList
