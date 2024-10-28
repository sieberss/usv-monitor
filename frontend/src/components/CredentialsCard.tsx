import {Credentials} from "../types/credentials.ts";
import {useNavigate} from "react-router-dom";
import {FaPlus} from "react-icons/fa";

type UpsCardProps = {
    credentials: Credentials
}

export default function CredentialsCard(props: Readonly<UpsCardProps>){
    const navigate = useNavigate()
    return(
        <div onClick={() => navigate("/credentials/" + props.credentials.id)}>
            {props.credentials.id !== "new"
              ? <>
                <h3>{props.credentials.user}</h3>
                </>
                : // plus-button for adding
                 <h2><FaPlus/></h2>
            }
        </div>
    )
}