type Props = {
    editing: boolean,
    name: string,
    nameInput: string,
    setNameInput: (newValue: string) => void
    address: string,
    addressInput: string,
    setAddressInput: (newValue: string) => void,
    setChangedData: (changed: boolean) => void
}

export default function NameAndAddressInputFields(props: Readonly<Props>){
    const nameInputField = <input
        id={'name'}
        type={'text'}
        name={'name'}
        value={props.nameInput}
        onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
            props.setNameInput(event.target.value)
            props.setChangedData(true)
        }}
    />;

    const addressInputField = <input
        id={'address'}
        type={'text'}
        name={'address'}
        value={props.addressInput}
        onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
            props.setAddressInput(event.target.value)
            props.setChangedData(true)
        }}
    />;


    return(
        <>
            <li>
                <label htmlFor={'name'}>Name:</label>
                {props.editing
                    ? nameInputField
                    : <p>{props.name}</p>}
            </li>
            <li>
                <label htmlFor={'address'}>Address (IP or FQDN):</label>
                {props.editing
                    ? addressInputField
                    : <p>{props.address}</p>}
            </li>
        </>
    )
}