import UsvContentDisplayAndEditing from "../components/UsvContentDisplayAndEditing.tsx";

interface AddUsvPageProps {
    usvUpdate: () => void
}

export default function AddUsvPage(props: AddUsvPageProps) {
    return (
        <UsvContentDisplayAndEditing usv={{id: "new", name: "", address: "", community: ""}}
                                     usvUpdate={props.usvUpdate}/>
    )
}