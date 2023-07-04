import React, { useEffect, useState } from 'react';
import './KafkaRecord.css';
// import NavBar from "./Navbar";
import { Link, useParams} from 'react-router-dom';
import { FaEdit, FaTrash } from "react-icons/fa";
import applicationService from '../sevices/application.service';
import axios from 'axios';
import NavBar from './Navbar';

const TopicList = () => {

    const [TopicRecord, setTopicRecord] = useState([]);
  
    const params = useParams();
    
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

    useEffect(() => {
        const fetchData = async () => {
          try {
                await axios.get(applicationService.gettopiccluster(`${params.id}`))
            .then(res => {
                         console.log(res)
                         setTopicRecord(res.data)
            })
            // Process the response data
          } catch (error) {
            if (error.response) {
              // Request was made and server responded with a status code outside the 2xx range
              console.log(error.response.data); // Response data from the server
              console.log(error.response.status); // Response status code
              console.log(error.response.headers); // Response headers
            } else if (error.request) {
              // Request was made but no response was received
              console.log(error.request);
            } else {
              // Something else happened while setting up the request
              console.log('Error', error.message);
            }
            console.log(error.config); // Config used to make the request
          }
        };
    
        fetchData();
      }, [params.id])

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
                            <table className="table table-borderd">
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
                                            <tr key={topic.id}>
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
                                                <td>{topic.monitoringstatus}</td>
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
                </div>
            </div>

        </>
    )
}

export default TopicList
