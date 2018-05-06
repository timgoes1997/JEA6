import {Pipe, PipeTransform} from '@angular/core';
import {Kweet} from '../entities/Kweet';

@Pipe({
  name: 'kweetMessage'
})
export class KweetMessagePipe implements PipeTransform {

  transform(kweet: Kweet, args?: any): string {
    if (kweet.text.indexOf('<i>') !== -1) {
      return kweet.text.replace('<i>', '').replace('<\/i>', '');
    }
  }

}
