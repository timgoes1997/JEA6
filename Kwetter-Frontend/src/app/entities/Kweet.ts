import {Tag} from './Tag';
import {User} from './User';
import {Link} from './Link';

export class Kweet {
  constructor(
    public id: number,
    public discriminator: number,
    public date: string,
    public text: string,
    public type: string,
    public tags: Tag[],
    public likes: User[],
    public links: Link[],
    public mentions: User[],
    public messager: User) {
  }
}
