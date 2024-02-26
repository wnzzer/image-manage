export interface loginForm{
    username : string;
    password : string;
}

export interface userInformation extends loginForm{
    level : number,
    uuid : string,
    token : string
}