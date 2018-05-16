export interface User {
  username: string;
  authorities: Authority[]
  enabled: string;
  authority: string[];


}
export interface Authority {
  authority:string;
}

