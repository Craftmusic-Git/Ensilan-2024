import {Injectable, OnDestroy} from "@angular/core";
import {BaseMessage} from "@domain/message/base.message";
import {CompatClient, Stomp, StompSubscription} from "@stomp/stompjs";
import {Subject} from "rxjs";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class GamesWebsocket implements OnDestroy {
  private connection: CompatClient;
  private subscriptions: StompSubscription;

  private messageSubject = new Subject<BaseMessage>();
  message$ = this.messageSubject.asObservable();

  constructor() {
    this.connection = Stomp.client(environment.wsUrl);
    this.connection.connect({}, () => {
      console.log('Games websocket Connected to WS')
      this.subscriptions = this.connection.subscribe('/topic/games', (message) => {
        console.log('Message' ,message);
        this.messageSubject.next(JSON.parse(message.body) as BaseMessage);
      });
    });
  }

  ngOnDestroy(): void {
    if (this.subscriptions) {
      this.subscriptions.unsubscribe();
    }
    this.connection.disconnect();
  }
}
