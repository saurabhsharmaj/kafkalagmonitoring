import React, { useState } from 'react';
import axios from 'axios';
import './KafkaEntityUploadForm.css';
//import httpCommon from "../http-common";

const CustomerUploadForm = () => {
    const [selectedFile, setSelectedFile] = useState(null);
    const [uploadMessage, setUploadMessage] = useState('');
  
    const handleFileChange = (event) => {
      setSelectedFile(event.target.files[0]);
    };
  
    const handleFileUpload = () => {
      const formData = new FormData();
      formData.append('file', selectedFile);
  
      axios
        .post('http://localhost:8081/api/uploadfile', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        })
        .then((response) => {
          setUploadMessage(response.data.message);
          window.location.href = "/topic";
        })
        .catch((error) => {
          setUploadMessage('Please Upload an CSV file');
          console.error(error);
        });

    };
    const CSV_FILE_URL ='http://localhost:3000/kafkaEntity.csv';
    const downloadFileAtURL=(url)=>{
      const fileName = url.split("/").pop();
      const aTag = document.createElement("a");
      aTag.href=url;
      aTag.setAttribute("download", fileName);
      document.body.appendChild(aTag);
      aTag.click();
      aTag.remove();
    }
    return (
    <>
        <div>
            <input type="file" onChange={handleFileChange} className='file_upload'/>
            <button onClick={handleFileUpload} className='  upload_button'>Upload</button>
            <button onClick={()=>{downloadFileAtURL(CSV_FILE_URL)}} className='sample_button'>Download Sample file</button>
            {uploadMessage && <p>{uploadMessage}</p>}
        </div>
    </>
    );
  };

export default CustomerUploadForm;