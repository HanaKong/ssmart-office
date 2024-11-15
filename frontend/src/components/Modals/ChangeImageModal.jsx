import { PropTypes } from "prop-types";
import ReactModal from "react-modal";

import Close from "@/assets/Modals/Close.svg?react";

import styles from "@/styles/Modals/ChangeImageModal.module.css";
import ImageUpload from "@/components/ImageUpload";
import { useState } from "react";
import { updateImageFile } from "@/services/fileAPI";
import { updateProfile } from "@/services/myInfoAPI";
import useMyInfoStore from "@/store/useMyInfoStore";

const ChangeImageModal = ({ onSubmit, onClose }) => {
  const [selectedImage, setSelectedImage] = useState();
  const updateProfileImage = useMyInfoStore(
    (state) => state.updateProfileImage
  );

  const handleImageSelect = (file) => {
    setSelectedImage(file);
  };

  const handleClickSubmit = async () => {
    const maxFileSize = 5 * 1024 * 1024; // 5MB
    if (!selectedImage) {
      alert("이미지를 선택해주세요.");
      return;
    } else if (selectedImage.size > maxFileSize) {
      alert("이미지 파일 크기는 5MB 이하여야 합니다.");
      return;
    } else {
      try {
        const response = await updateImageFile(selectedImage);
        let imageUrl = response.data;
        if (response.status === 200 || response.status === 201) {
          await updateProfile(imageUrl);
          // store 사진 업데이트 하기
          updateProfileImage(imageUrl);
        }
        onSubmit();
      } catch (error) {
        console.error("이미지 업로드 실패:", error);
      }
    }
  };

  const handleClickCancel = () => {
    onClose();
  };

  return (
    <ReactModal
      isOpen={true}
      style={{
        overlay: {
          position: "fixed",
          top: 0,
          left: 0,
          right: 0,
          bottom: 0,
          backgroundColor: "rgba(0, 0, 0, 0.75)",
        },
        content: {
          position: "absolute",
          width: "400px",
          height: "500px",
          top: "50%",
          left: "50%",
          transform: "translate(-50%, -50%)",
          background: "#fff",
          overflow: "auto",
          WebkitOverflowScrolling: "touch",
          borderRadius: "20px",
          outline: "none",
          padding: "20px",
        },
      }}
    >
      <button className={styles.close} onClick={handleClickCancel}>
        <Close />
      </button>
      <div className={styles.container}>
        <h1 className={styles.title}>프로필 이미지 변경</h1>
        <h3 className={styles.subTitle}>나를 표현하는 이미지를 등록하세요</h3>
        <div>
          <div className={styles.imageBox}>
            <ImageUpload onImageSelect={handleImageSelect} />
          </div>
        </div>
        <div className={styles.buttonBox}>
          <button
            onClick={handleClickSubmit}
            className={`${styles.confirm} ${styles.button}`}
          >
            사진 업로드
          </button>
        </div>
      </div>
    </ReactModal>
  );
};

ChangeImageModal.propTypes = {
  onSubmit: PropTypes.func.isRequired,
  onClose: PropTypes.func.isRequired,
};
export default ChangeImageModal;
