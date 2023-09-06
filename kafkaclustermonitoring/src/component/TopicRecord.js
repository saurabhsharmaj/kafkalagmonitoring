import React, { useEffect, useState } from 'react';
import './TopicRecord.css';
import { Link} from 'react-router-dom';
import { FaTrash ,FaEdit, FaPlus} from "react-icons/fa";
import DataTable from 'react-data-table-component';
import applicationService from '../sevices/application.service';
import TopHeader from './Header';

const TopicList = () => {

    const [TopicRecord, setTopicRecord] = useState([]);
    const [search, setSearch] = useState("");
    const [filter, setFilter] = useState([]);
  
    // const getTopics = async() =>{
    //     try{
    //         const response = await axios.get(applicationService.getTopic());
    //         setTopicRecord(response);
    //     }catch(error){
    //         console.log(error);
    //     }
    // };
    

    const getTopics = async() => {
      applicationService.getTopic()
        .then(response => {
          console.log('Printing Topic data', response.data);
          setTopicRecord(response.data);
          setFilter(response.data);
        })
        .catch(error => {
          console.log('Something went wrong', error);
        }) 
    }

    const column =[
        {
            name: "S no.",
            selector: (row,index)=> index+1, 
            sortable: true                 
        },
        {
            name: "Consumer Group",
            selector: (row) => row.consumergroup,
            sortable: true,
        },
        {
            name: "Topic Name",
            selector: (row) => row.topicname
        },
        {
            name: "Owner",
            selector: (row) => row.owner
        },
        {
            name: "Email Id",
            selector: (row) => row.emailid,
            
        },
        {
            name: "Monitoring Status",
            selector: (row) => row.monitoringstatus
        },
        {
            name: "Threshold value",
            selector: (row) => row.threshold,
            sortable: true
        },
        {
            name: "Description",
            selector: (row) => row.description
        },
        {
            name: "Edit",
            cell: (row) => (
                <Link to={`/kafka/edit/${row.groupid}`}>
                    <FaEdit className='icon_click'/>
                </Link>                
            )
        },        
        {
            name: "Update",
            cell: (row) => (
                <Link to>
                <FaTrash className='icon_click' onClick={() => {
                    handleDelete(row.groupid);
                    }}/>
                </Link>
            )
        }        
        
    ]
    useEffect(() => {
      getTopics();
    }, []);
    
    useEffect(()=>{
        const result = TopicRecord.filter(topics => {
            return topics.topicname.toLowerCase().match(search.toLowerCase());
        });
        setFilter(result);
    },[TopicRecord,search]);
    
    const handleDelete = (groupid) => {
        console.log('Printing id', groupid);
        if(window.confirm("Do you want to delete...??")){
            applicationService.removetopic(groupid)
            .then(response => {
            console.log(response.data);
            getTopics();            
          })
          .catch(error => {
            console.log('Something went wrong', error);
          })
        }
    }

    return(
        <>
        <TopHeader/>
        <DataTable 
        className='topic-list'
        title="Topic List"        
        columns={column} 
        data={filter} 
        pagination
        fixedHeader
        highlightOnHover
        subHeader
        subHeaderComponent={
            <input type = "text"
            placeholder='search by topic name'
            className="w-25 form-control"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            />
        }
        />
        <Link to = '/kafka' className='add-cluster'><FaPlus/>Add Topic</Link>
        </>
    )
}

export default TopicList
