import {Tag} from './Tag';
import {User} from './User';

export class Kweet {
  constructor(
    public id: number,
    public discriminator: number,
    public date: string,
    public text: string,
    public type: string,
    public tags: Tag[],
    public likes: User[],
    public mentions: User[],
    public messager: User) {

  }
}
