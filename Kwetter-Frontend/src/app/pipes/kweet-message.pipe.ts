import {Pipe, PipeTransform} from '@angular/core';
import {Kweet} from '../entities/Kweet';

@Pipe({
  name: 'kweetMessage'
})
export class KweetMessagePipe implements PipeTransform {

  transform(kweet: Kweet, args?: any): string {
    let message = kweet.text;
    message = this.generateMentions(kweet, message);
    message = this.generateTags(kweet, message);

    // TODO: Remove other html tags before generating mentions and hashtags.
    if (message.indexOf('<i>') !== -1) {
      return message.replace('<i>', '').replace('<\/i>', '');
    }
  }

  generateMentions(kweet: Kweet, message: string): string {
    let text = message;
    for (const mention of kweet.mentions) {
      text = text.replace(`@${mention.username}`,
        `<a ng-reflect-router-link="/app-user, ${mention.username}" href="/user/${mention.username}">@${mention.username}</a>`);
    }
    return text;
  }

  generateTags(kweet: Kweet, message: string): string {
    let lower = message.toLowerCase();
    let text = message;
    for (const tag of kweet.tags) {
      const find = lower.search(`#${tag.tagName}`);
      const sub = text.slice(find, find + tag.tagName.length + 1);
      text = text.replace(sub,
        `<a ng-reflect-router-link="/app-tag-details, ${tag.tagName}" href="/tag/${tag.tagName}">${sub}</a>`);
      // <a ng-reflect-router-link="/app-tag-details, ${tag.tagName}" href="/tag/${tag.tagName}">#${sub}</a>
      lower = text.toLowerCase();
    }
    return text;
  }
}
