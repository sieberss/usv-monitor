import {Ups} from "../types/ups.ts";
import {useNavigate} from "react-router-dom";
import {FaPlus} from "react-icons/fa";
import './UpsCard.css'

type UpsCardProps = {
    ups: Ups,
    monitoring: boolean
}

export default function UpsCard(props: Readonly<UpsCardProps>){
    const navigate = useNavigate()
    return(
        <div onClick={() => navigate("/upsdetails/" + props.ups?.id)}>
            {props.ups?.id !== "new"
              ? <>
                <h3>{props.ups?.name}</h3>
                <p>{props.ups?.address}</p>
                </>
                : // plus-button for adding
                 <h2><FaPlus/></h2>
            }
        </div>
    )
}